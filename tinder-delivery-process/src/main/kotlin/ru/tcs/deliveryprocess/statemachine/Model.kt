package ru.tcs.deliveryprocess.statemachine

import com.tinder.StateMachine
import ru.tcs.deliveryprocess.statemachine.repository.StateRepository

class Model(
    val businessId: String,
    val variables: MutableMap<String, String> = mutableMapOf(),
    private val initState: State = State.Start
) {

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

