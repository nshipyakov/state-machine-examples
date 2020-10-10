package ru.tcs.deliveryprocess.statemachine.repository

import ru.tcs.deliveryprocess.statemachine.Model
import java.util.concurrent.ConcurrentHashMap

class StateRepository {

    private val repo = ConcurrentHashMap<String, Model>()

    fun saveModel(model: Model) {
        repo[model.businessId] = model
    }

    fun getActiveModels(count: Int = 10): List<Model> {
        return repo.values.take(count)
    }

    fun getModelByBusinessId(businessId: String): Model {
        return checkNotNull(repo[businessId])
    }
}
