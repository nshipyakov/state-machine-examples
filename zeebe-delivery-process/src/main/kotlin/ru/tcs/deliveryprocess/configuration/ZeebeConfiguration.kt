package ru.tcs.deliveryprocess.configuration

import io.zeebe.client.ZeebeClient
import ru.tcs.deliveryprocess.worker.CollectOrderWorker
import ru.tcs.deliveryprocess.worker.SendOrderWorker

fun configureZeebeClient(contactPoint: String): ZeebeClient {
    val client = zeebeClient(contactPoint)
    val process = deployProcess(client, "bpmn/delivery-process.bpmn")
    CollectOrderWorker(client)
    SendOrderWorker(client)
    return client
}

private fun zeebeClient(contactPoint: String) = ZeebeClient.newClientBuilder()
    .brokerContactPoint(contactPoint)
    .usePlaintext()
    .build()

private fun deployProcess(client: ZeebeClient, process: String) =
    client.newDeployCommand()
        .addResourceFromClasspath(process)
        .send()
        .join()

