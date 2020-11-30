package ru.tinkoff.delivery.state.spring.springstatedelivery.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.statemachine.StateMachinePersist
import org.springframework.statemachine.data.mongodb.MongoDbPersistingStateMachineInterceptor
import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository
import org.springframework.statemachine.persist.DefaultStateMachinePersister
import org.springframework.statemachine.persist.StateMachinePersister
import org.springframework.statemachine.persist.StateMachineRuntimePersister
import ru.tinkoff.delivery.state.spring.springstatedelivery.core.Events
import ru.tinkoff.delivery.state.spring.springstatedelivery.core.States
import ru.tinkoff.delivery.state.spring.springstatedelivery.persist.CustomStateMachinePersist
import ru.tinkoff.delivery.state.spring.springstatedelivery.persist.InMemoryStateMachinePersist
import java.util.*

@Configuration
class PersistConfiguration {
    @Bean
    @Profile("in-memory", "default")
    fun inMemoryPersist(): StateMachinePersist<States, Events, String> {
        return InMemoryStateMachinePersist()
    }

    @Bean
    @Profile("custom")
    fun customPersist(): StateMachinePersist<States, Events, String> {
        return CustomStateMachinePersist()
    }

    @Bean
    @Profile("mongo")
    fun mongoRuntimePersist(
        repository: MongoDbStateMachineRepository?): StateMachineRuntimePersister<States, Events, String> {
        return MongoDbPersistingStateMachineInterceptor<States, Events, String>(repository)
    }

    @Bean
    fun persister(): StateMachinePersister<States, Events, String> {
        return DefaultStateMachinePersister<States, Events, String>(inMemoryPersist())
    }

}
