package ru.tcs.deliveryprocess.statemachine.repository

import ru.tcs.deliveryprocess.statemachine.Model
import ru.tcs.deliveryprocess.statemachine.State
import java.util.concurrent.ConcurrentHashMap

class StateRepository {

    private val repo = ConcurrentHashMap<String, Model>()

    fun saveModel(model: Model) {
        repo[model.businessId] = model
    }

    fun getActiveModels(count: Int = 10): List<Model> {
        return repo.values.take(count)
    }

    fun getModelsByState(state: State): List<Model> = repo.values.filter { it.state.state == state }

    fun getModelByBusinessId(businessId: String): Model {
        return checkNotNull(repo[businessId])
    }
}
