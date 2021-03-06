package ru.tcs.deliveryprocess

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.tcs.deliveryprocess.statemachine.Event
import ru.tcs.deliveryprocess.statemachine.Model
import ru.tcs.deliveryprocess.statemachine.Poller
import ru.tcs.deliveryprocess.statemachine.State
import ru.tcs.deliveryprocess.statemachine.repository.StateRepository

private val mapper  = ObjectMapper()
private val typeRef = object : TypeReference<HashMap<String, String>>() {}

val repository = StateRepository()

fun Application.routes(){

    routing {
        get("/collect") {
            repository.getModelByBusinessId("123")
                .state
                .transition(Event.OnCollect)
        }
        get("/deliver") {
            repository.getModelByBusinessId("123")
                .state
                .transition(Event.OnReceive)
        }
        get("/start") {
            Model(businessId = "123").makeStateMachine(repository).start()
        }
    }
}

fun main() {

    GlobalScope.launch {
        Poller(repository).startPolling()
    }
    embeddedServer(Netty, 8081, module = Application::routes).apply {
        start(wait = false)
    }
}
