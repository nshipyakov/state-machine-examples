package ru.tcs.deliveryprocess.statemachine

import kotlinx.coroutines.delay
import ru.tcs.deliveryprocess.statemachine.repository.StateRepository
import java.time.LocalDateTime

class Poller(private val repository: StateRepository) {

    private val continuePolling = true

    suspend fun startPolling() {
        while (continuePolling) {
            repository.getActiveModels(10)
                .forEach{
                    if (it.isRetryReady()) {
                        it.state.transition(checkNotNull(it.lastEvent))
                    }
                    if (it.isTimeoutReady()) {
                        it.state.transition(Event.OnTimeUp)
                    }
                }
            delay(1000)
        }
    }
}

private fun Model.isTimeoutReady() = this.state.state.timerValueInSeconds > 0 &&
    this.lastActivityTime?.plusSeconds(this.state.state.timerValueInSeconds)?.isBefore(LocalDateTime.now()) ?: false

private fun Model.isRetryReady() = this.lastEvent != null && this.timeToTryAgain?.isBefore(LocalDateTime.now()) ?: false
