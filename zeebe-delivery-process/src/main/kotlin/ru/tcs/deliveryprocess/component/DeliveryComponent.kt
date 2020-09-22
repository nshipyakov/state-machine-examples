package ru.tcs.deliveryprocess.component

import io.zeebe.client.ZeebeClient

class DeliveryComponent(private val zeebeClient: ZeebeClient) {

    fun deliver(correlationKey: String, value: String) {
        zeebeClient.newPublishMessageCommand()
            .messageName("WaitOrderMessage")
            .correlationKey(correlationKey)
            .variables(mapOf("newValue" to value))
            .send()
    }
}
