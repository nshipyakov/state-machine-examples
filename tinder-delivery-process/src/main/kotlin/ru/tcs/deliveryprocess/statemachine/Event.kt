package ru.tcs.deliveryprocess.statemachine

sealed class Event {

    object OnExecuteStep: Event()
    object OnCollect: Event()
    object OnReceive: Event()
    object OnTimeUp: Event()

    companion object {

        fun getEvent(name: String?) = when(name) {
            null -> null
            "OnExecuteStep" -> OnExecuteStep
            "OnCollect" -> OnCollect
            "OnReceive" -> OnReceive
            "OnTimeUp" -> OnTimeUp
            else ->throw RuntimeException("Can't find event")
        }
    }
}
