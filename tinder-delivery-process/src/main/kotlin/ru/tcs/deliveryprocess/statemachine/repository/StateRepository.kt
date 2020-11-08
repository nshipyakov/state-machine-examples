package ru.tcs.deliveryprocess.statemachine.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import ru.tcs.deliveryprocess.statemachine.Event
import ru.tcs.deliveryprocess.statemachine.Model
import ru.tcs.deliveryprocess.statemachine.State
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

private val OBJECT_MAPPER = ObjectMapper().apply {
    this.registerModule(
        JavaTimeModule()
    )
    this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}

// TODO Надо околонормальный сторадж чтобы была возможность проверять ретраи  и восстановления
class StateRepository {

    private val repo = ConcurrentHashMap<String, String>()

    fun saveModel(model: Model) {
        repo[model.businessId] = OBJECT_MAPPER.writeValueAsString(model.mapToInstanceEntity())
    }

    fun getActiveModels(count: Int = 10): List<Model> {
        return repo.values.asSequence()
            .map { OBJECT_MAPPER.readValue(it, InstanceEntity::class.java) }
            .map { it.mapToModel() }
            .toList()
    }

    fun getModelByBusinessId(businessId: String): Model {
        return checkNotNull(OBJECT_MAPPER.readValue(repo[businessId], InstanceEntity::class.java)).mapToModel()
    }

    private fun Model.mapToInstanceEntity() = InstanceEntity(
        this.businessId,
        this.variables,
        this.state.state.javaClass.simpleName.toString(),
        this.startTime,
        this.lastActivityTime,
        this.timeToTryAgain,
        this.attemptsCount,
        this.lastEvent?.javaClass?.simpleName?.toString()
    )

    private fun InstanceEntity.mapToModel() = Model(
        requireNotNull(this.businessId),
        this.variables,
        requireNotNull(State.getState(requireNotNull(this.state.also { println(it) })))
    ).also {
        it.startTime = this.startTime
        it.lastActivityTime = this.lastActivityTime
        it.timeToTryAgain = this.timeToTryAgain
        it.attemptsCount = this.attemptsCount
        it.lastEvent = Event.getEvent(this.lastEvent)
        it.makeStateMachine(this@StateRepository)
    }
}

class InstanceEntity(
    var businessId: String? = null,
    var variables: MutableMap<String, String> = mutableMapOf(),
    var state: String? = null,
    var startTime: LocalDateTime? = null,
    var lastActivityTime: LocalDateTime? = null,
    var timeToTryAgain: LocalDateTime? = null,
    var attemptsCount: Int? = 0,
    var lastEvent: String? = null
)

