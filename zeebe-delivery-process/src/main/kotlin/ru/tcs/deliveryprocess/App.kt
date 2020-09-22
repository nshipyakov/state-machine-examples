package ru.tcs.deliveryprocess

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import ru.tcs.deliveryprocess.component.CollectComponent
import ru.tcs.deliveryprocess.component.DeliveryComponent
import ru.tcs.deliveryprocess.component.StartProcessComponent
import ru.tcs.deliveryprocess.configuration.applicationModules

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
}

fun Application.routes(){
    val collectComponent: CollectComponent by inject(CollectComponent::class.java)
    val deliveryComponent: DeliveryComponent by inject(DeliveryComponent::class.java)
    val startProcessComponent: StartProcessComponent by inject(StartProcessComponent::class.java)

    routing {
        get("/collect") {
            collectComponent.collect("123456789", "v1")
        }
        get("/deliver") {
            deliveryComponent.deliver("123456789", "v2")
        }
        get("/start") {
            startProcessComponent.start()
        }
    }
}

fun main(args: Array<String>) {
    startKoin {
        modules(applicationModules)
    }
    embeddedServer(Netty, 8081, module = Application::routes).apply {
        start(wait = false)
    }
}
