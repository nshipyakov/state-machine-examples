package ru.tinkoff.delivery.state.spring.springstatedelivery.persist

import mu.KLogging
import org.springframework.statemachine.StateMachineContext
import org.springframework.statemachine.StateMachinePersist
import ru.tinkoff.delivery.state.spring.springstatedelivery.core.Events
import ru.tinkoff.delivery.state.spring.springstatedelivery.core.States
import java.util.*

class CustomStateMachinePersist : StateMachinePersist<States, Events, String> {
    private val storage: HashMap<String, StateMachineContext<States, Events>> = HashMap()

    override fun write(context: StateMachineContext<States, Events>, contextObj: String) {
        logger.warn("!!!push: $contextObj")
        storage[contextObj] = context
    }


    override fun read(contextObj: String): StateMachineContext<States, Events> {
        logger.warn("!!!pop: $contextObj")
        return storage[contextObj]!!
    }

    companion object : KLogging()
}
