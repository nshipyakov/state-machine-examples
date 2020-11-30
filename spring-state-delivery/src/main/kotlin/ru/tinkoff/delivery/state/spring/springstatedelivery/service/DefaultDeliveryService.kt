package ru.tinkoff.delivery.state.spring.springstatedelivery.service

import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.config.StateMachineFactory
import org.springframework.statemachine.persist.StateMachinePersister
import org.springframework.stereotype.Service
import ru.tinkoff.delivery.state.spring.springstatedelivery.core.Events
import ru.tinkoff.delivery.state.spring.springstatedelivery.core.States

@Service
class DefaultDeliveryService(
    val persister: StateMachinePersister<States, Events, String>,
    val factory: StateMachineFactory<States, Events>
): DeliveryService {
    override fun start(businessId: String) {
        val stateMachine: StateMachine<States, Events> = factory.stateMachine
        stateMachine.extendedState.variables["BUSINESS_ID"] = businessId
        stateMachine.start()
        try {
            persister.persist(stateMachine, businessId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun deliver(businessId: String) {
        val stateMachine: StateMachine<States, Events> = factory.stateMachine
        try {
            persister.restore(stateMachine, businessId)
            stateMachine.sendEvent(Events.ON_RECEIVE)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun collect(businessId: String) {
        val stateMachine: StateMachine<States, Events> = factory.stateMachine
        try {
            persister.restore(stateMachine, businessId)
            stateMachine.sendEvent(Events.ON_COLLECT)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
