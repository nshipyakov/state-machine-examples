package ru.tcs.deliveryprocess.statemachine

sealed class State(val timerValueInSeconds: Long = 0) {
    object Start: State()
    object WaitingOfCollecting: State()
    object ReadyToSend: State()
    object WaitingOrder: State(20)
    object End: State()
}
