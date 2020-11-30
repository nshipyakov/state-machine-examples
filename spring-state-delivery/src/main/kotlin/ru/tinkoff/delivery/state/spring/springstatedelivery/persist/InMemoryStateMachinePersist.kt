package ru.tinkoff.delivery.state.spring.springstatedelivery.persist

import org.springframework.statemachine.StateMachineContext
import org.springframework.statemachine.StateMachinePersist
import ru.tinkoff.delivery.state.spring.springstatedelivery.core.Events
import ru.tinkoff.delivery.state.spring.springstatedelivery.core.States
import java.lang.IllegalArgumentException
import java.util.*

class InMemoryStateMachinePersist : StateMachinePersist<States, Events, String> {
    private val storage: HashMap<String, StateMachineContext<States, Events>> = HashMap()

    override fun write(context: StateMachineContext<States, Events>, contextObj: String) {
        storage[contextObj] = context
    }

    override fun read(contextObj: String): StateMachineContext<States, Events> {
        return storage[contextObj] ?: throw Exception("Couldn't find process with this context!")
    }
}
