package ru.tcs.deliveryprocess.statemachine

import com.tinder.StateMachine
import ru.tcs.deliveryprocess.statemachine.repository.StateRepository
import java.util.*

fun makeStateMachine(initialState: State, model: Model, repository: StateRepository) = StateMachine.create<State, Event, SideEffect> {
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
    }
    onTransition {
        val validTransition = it as? StateMachine.Transition.Valid ?: return@onTransition
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
        println(model.state.state)
        repository.saveModel(model)
    }
}
