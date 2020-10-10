package ru.tcs.deliveryprocess.statemachine

sealed class SideEffect {

    object Start : SideEffect()
    object Send : SideEffect()
}
