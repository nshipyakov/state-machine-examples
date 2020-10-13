package ru.tcs.deliveryprocess.statemachine

import com.tinder.StateMachine
import ru.tcs.deliveryprocess.statemachine.repository.StateRepository
import java.time.LocalDateTime

// TODO Таймаут как-то работает
// TODO Ретраи - не особо сложно
// TODO Абстракции, а то сейчас все в перемешку
class Model(
    val businessId: String,
    val variables: MutableMap<String, String> = mutableMapOf(),
    private val initState: State = State.Start
) {

    var startTime: LocalDateTime? = null
    var lastActivityTime: LocalDateTime? = null
    private var localState: StateMachine<State, Event, SideEffect>? = null

    val state: StateMachine<State, Event, SideEffect>
        get() = checkNotNull(localState)

    fun makeStateMachine(repository:  StateRepository): Model {
        localState = makeStateMachine(initState, this, repository)
        return this
    }

    fun start() {
        state.transition(Event.OnExecuteStep)
    }
}

