package ru.tcs.deliveryprocess.configuration

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.tcs.deliveryprocess.component.CollectComponent
import ru.tcs.deliveryprocess.component.DeliveryComponent
import ru.tcs.deliveryprocess.component.StartProcessComponent
import ru.tcs.deliveryprocess.worker.CollectOrderWorker
import ru.tcs.deliveryprocess.worker.SendApologyWorker
import ru.tcs.deliveryprocess.worker.SendOrderWorker

fun Module.configureZeebeClient() {
    single { configureZeebeClient("127.0.0.1:26500") }
}

fun Module.configureWorkers() {
    single { CollectOrderWorker(get()) }
    single { SendOrderWorker(get()) }
    single { SendApologyWorker(get()) }
}

fun Module.configureComponents() {
    single { CollectComponent(get()) }
    single { DeliveryComponent(get()) }
    single { StartProcessComponent(get()) }
}

val applicationModules = module {
    configureZeebeClient()
    configureWorkers()
    configureComponents()
}
