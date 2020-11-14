package ru.tcs.deliveryprocess.statemachine

import com.tinder.StateMachine
import ru.tcs.deliveryprocess.statemachine.repository.StateRepository
import java.time.LocalDateTime
import java.util.*

fun makeStateMachine(
    initialState: State,
    model: Model,
    repository: StateRepository
) = StateMachine.create<State, Event, SideEffect> {
    initialState(initialState)
    state<State.Start> {
        on<Event.OnExecuteStep> {
            transitionTo(State.WaitingOfCollecting, SideEffect.Start)
        }
    }
    state<State.WaitingOfCollecting> {
        on<Event.OnCollect> {
            transitionTo(State.WaitingOrder, SideEffect.Send)
        }
    }
    state<State.ReadyToSend> {
        on<Event.OnExecuteStep> {
            transitionTo(State.WaitingOrder, SideEffect.Send)
        }
    }
    state<State.WaitingOrder> {
        on<Event.OnReceive> {
            transitionTo(State.End)
        }
        on<Event.OnTimeUp> {
            transitionTo(State.WaitingOfCollecting, SideEffect.Start)
        }
    }
    onTransition {
        val validTransition = it as? StateMachine.Transition.Valid
        if(model.startTime == null) {
            model.startTime = LocalDateTime.now()
        }
        model.lastActivityTime = LocalDateTime.now()
        if (validTransition == null) {
            repository.saveModel(model)
            return@onTransition
        }
        model.timeToTryAgain = null
        if (validTransition != null) {
            val availableRetryCount = validTransition.sideEffect?.retry?.count ?: 0
            if (availableRetryCount > 0 && model.attemptsCount ?: 0 > availableRetryCount) {
                return@onTransition
            }
            try {
                when (validTransition.sideEffect) {
                    SideEffect.Start -> {
                        println("Start step")
                        println(model.variables)
                        model.variables["orderId"] = UUID.randomUUID().toString()
                    }
                    SideEffect.Send -> {
                        println("Send step")
                        println(model.variables)
                        model.variables["mailId"] = UUID.randomUUID().toString()
                    }
                }
            } catch (e: Exception) {
                val oldModelVersion = repository.getModelByBusinessId(model.businessId)
                val retryInfo = validTransition.sideEffect?.retry ?: return@onTransition
                oldModelVersion.timeToTryAgain = LocalDateTime.now().plusSeconds(retryInfo.timerValueInSeconds)
                oldModelVersion.lastErrorSideEffect = validTransition.sideEffect
                oldModelVersion.lastEvent = it.event
                oldModelVersion.attemptsCount = (oldModelVersion.attemptsCount ?: 0) + 1
                if (availableRetryCount == oldModelVersion.attemptsCount ?: 0) {
                    oldModelVersion.isError = true
                }
                repository.saveModel(oldModelVersion)
                return@onTransition
            }
        }
        model.timeToTryAgain = null
        model.attemptsCount = null
        repository.saveModel(model)
        println("state = ${model.state.state}  time = ${model.lastActivityTime}")
        model.state.transition(Event.OnExecuteStep)
    }
}
