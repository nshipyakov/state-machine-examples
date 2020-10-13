package ru.tcs.deliveryprocess.statemachine

import kotlinx.coroutines.delay
import ru.tcs.deliveryprocess.statemachine.repository.StateRepository
import java.time.LocalDateTime

class Poller(private val repository: StateRepository) {

    private val continuePolling = true

    suspend fun startPolling() {
        while (continuePolling) {
            repository.getActiveModels(10)
                .filter {
                    it.state.state.timerValueInSeconds > 0 &&
                    it.lastActivityTime?.plusSeconds(it.state.state.timerValueInSeconds)?.isBefore(LocalDateTime.now())
                        ?: false
                }
                .forEach{
                    it.state.transition(Event.OnTimeUp)
                }
            println("cycle polling. time = ${LocalDateTime.now()}")
            delay(1000)
        }
    }
}
