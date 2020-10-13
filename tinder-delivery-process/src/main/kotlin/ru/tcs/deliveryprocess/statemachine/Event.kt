package ru.tcs.deliveryprocess.statemachine

sealed class Event {
    object OnExecuteStep: Event()
    object OnCollect: Event()
    object OnReceive: Event()
    object OnTimeUp: Event()
}
