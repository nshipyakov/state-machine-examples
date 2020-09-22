package ru.tcs.deliveryprocess.component

import io.zeebe.client.ZeebeClient

class CollectComponent(private val zeebeClient: ZeebeClient) {

    fun collect(correlationKey: String, value: String) {
        zeebeClient.newPublishMessageCommand()
            .messageName("WaitOfCollectingMessage")
            .correlationKey(correlationKey)
            .variables(mapOf("newValue" to value))
            .send()
    }
}
