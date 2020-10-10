package ru.tcs.deliveryprocess.statemachine

sealed class State {
    object Start: State()
    object WaitingOfCollecting: State()
    object ReadyToSend: State()
    object WaitingOrder: State()
    object End: State()

    companion object {

        fun find(name: String): State {
            return checkNotNull(State::class.sealedSubclasses.first{
                it.simpleName == name
            }.objectInstance)
        }
    }
}
