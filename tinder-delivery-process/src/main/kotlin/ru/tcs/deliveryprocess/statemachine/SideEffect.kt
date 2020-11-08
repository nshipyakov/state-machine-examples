package ru.tcs.deliveryprocess.statemachine

sealed class SideEffect(val retry: Retry = Retry(3, 10)) {

    object Start : SideEffect()
    object Send : SideEffect()
}

class Retry(val count: Int = 0, val timerValueInSeconds: Long = 0)
