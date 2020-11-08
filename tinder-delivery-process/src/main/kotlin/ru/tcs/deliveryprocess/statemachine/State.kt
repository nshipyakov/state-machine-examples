package ru.tcs.deliveryprocess.statemachine

sealed class State(val timerValueInSeconds: Long = 0) {
    object Start: State()
    object WaitingOfCollecting: State()
    object ReadyToSend: State()
    object WaitingOrder: State(20)
    object End: State()

    companion object {

        fun getState(name: String) = when(name) {
            "Start" -> Start
            "WaitingOfCollecting" -> WaitingOfCollecting
            "ReadyToSend" -> ReadyToSend
            "WaitingOrder" -> WaitingOrder
            "End" -> End
            else ->throw RuntimeException("Can't find state")
        }
    }
}
