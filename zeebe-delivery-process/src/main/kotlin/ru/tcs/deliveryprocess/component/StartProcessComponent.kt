package ru.tcs.deliveryprocess.component

import io.zeebe.client.ZeebeClient
import java.util.*

class StartProcessComponent(private val zeebeClient: ZeebeClient) {

    fun start() {
        val wfInstance = zeebeClient.newCreateInstanceCommand()
            .bpmnProcessId("delivery-process")
            .latestVersion()
            .variables(mapOf("id" to UUID.randomUUID().toString()))
            .send()
            .join()
    }
}
