package ru.tinkoff.delivery.state.spring.springstatedelivery.configuration

import mu.KLogging
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.action.Action
import org.springframework.statemachine.config.EnableStateMachineFactory
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer
import org.springframework.statemachine.listener.StateMachineListener
import org.springframework.statemachine.listener.StateMachineListenerAdapter
import org.springframework.statemachine.state.State
import ru.tinkoff.delivery.state.spring.springstatedelivery.core.Events
import ru.tinkoff.delivery.state.spring.springstatedelivery.core.States

@Configuration
@EnableStateMachineFactory
class StateMachineConfig : EnumStateMachineConfigurerAdapter<States, Events>() {

    override fun configure(config: StateMachineConfigurationConfigurer<States, Events>) {
        config.withConfiguration()
            .listener(listener())
            .autoStartup(true)
    }

    private fun listener(): StateMachineListener<States, Events> {
        return object : StateMachineListenerAdapter<States, Events>() {

            override fun stateChanged(from: State<States, Events>?, to: State<States, Events>?) {
                from?.id?.let {
                    logger.info("Переход из статуса " + from.id + " в статус " + to!!.id)
                }
            }

            override fun stateMachineStarted(stateMachine: StateMachine<States, Events>?) {
                logger.info("state machine started")
            }

            override fun eventNotAccepted(event: Message<Events?>) {
                logger.error("Not accepted event: {}", event)
            }

/*            override fun transition(transition: Transition<States?, Events?>) {
                logger.warn("MOVE from: {}, to: {}",
                    ofNullableState(transition.source),
                    ofNullableState(transition.target))
            }

            private fun ofNullableState(s: State<*, *>): Any? {
                return Optional.ofNullable(s)
                    .map { obj: State<*, *> -> obj.id }
                    .orElse(null)
            }*/
        }
    }

    override fun configure(states: StateMachineStateConfigurer<States, Events>) {
        states.withStates()
            .initial(States.NEW, startToMakeOrder())
            .state(States.WAITING_FOR_COLLECTING, collectBox()) //todo: пока не юзаем
            .state(States.READY_TO_SEND, readyToSend())
            .state(States.WAITING_ORDER, waitForDuck())
            .state(States.DONE, done())
            .end(States.DONE)
    }

    private fun startToMakeOrder(): Action<States, Events> {
        return Action { logger.warn("Начинаем собирать уточку в дорогу") }
    }

    private fun collectBox(): Action<States?, Events?> {
        return Action { logger.warn("Упаковываем уточку") }
    }

    private fun readyToSend(): Action<States?, Events?> {
        return Action { logger.warn("Посылка собрана, готовы высылать!") }
    }

    private fun waitForDuck(): Action<States?, Events?> {
        return Action { logger.warn("Ждем, когда уточка приплывет") }
    }

    private fun done(): Action<States?, Events?> {
        return Action { logger.warn("Утка на месте, можно расходиться!") }
    }

    override fun configure(transitions: StateMachineTransitionConfigurer<States, Events>) {
        transitions
            .withExternal()
            .source(States.NEW)
            .target(States.WAITING_FOR_COLLECTING)
            .action(reservedAction(), errorAction())
            .and()
            .withExternal()
            .source(States.WAITING_FOR_COLLECTING)
            .target(States.READY_TO_SEND)
            .event(Events.ON_COLLECT)
            .action(cancelAction(), errorAction())
            .and()
            .withExternal()
            .source(States.READY_TO_SEND)
            .target(States.WAITING_ORDER)
            //.timer(10000)
            .action { logger.info("Время прошло") }
            .and()
            .withExternal()
            .source(States.WAITING_ORDER)
            .target(States.DONE)
            .event(Events.ON_RECEIVE)
            .action { logger.info("Утка на точке") }
    }

    private fun reservedAction(): Action<States, Events> {
        return Action {
            logger.warn("reservedAction")
        }
    }

    private fun cancelAction(): Action<States, Events> {
        return Action {
            logger.warn("cancelAction")
        }
    }

    private fun errorAction(): Action<States, Events> {
        return Action {
            logger.warn("errorAction")
        }
    }

    companion object : KLogging()
}
