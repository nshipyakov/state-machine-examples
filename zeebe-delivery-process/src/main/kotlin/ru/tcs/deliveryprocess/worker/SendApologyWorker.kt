package ru.tcs.deliveryprocess.worker

import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.response.ActivatedJob
import io.zeebe.client.api.worker.JobClient

class SendApologyWorker(client: ZeebeClient) : AbstractWorker(client,"send-email-with-apology") {

    override fun work(client: JobClient, job: ActivatedJob): Map<String, Any?> {
        Thread.sleep(100)
        return mapOf("orderId" to null)
    }
}
