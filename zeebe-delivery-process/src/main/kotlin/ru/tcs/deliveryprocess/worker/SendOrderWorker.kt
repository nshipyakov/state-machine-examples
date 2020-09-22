package ru.tcs.deliveryprocess.worker

import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.response.ActivatedJob
import io.zeebe.client.api.worker.JobClient

class SendOrderWorker(client: ZeebeClient): AbstractWorker(client, "send-order") {

    override fun work(client: JobClient, job: ActivatedJob): Map<String, Any> {
        Thread.sleep(200)
        return mapOf("packageId" to "987654321")
    }
}
