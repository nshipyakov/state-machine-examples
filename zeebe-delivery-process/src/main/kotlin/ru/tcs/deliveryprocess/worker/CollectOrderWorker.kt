package ru.tcs.deliveryprocess.worker

import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.response.ActivatedJob
import io.zeebe.client.api.worker.JobClient

class CollectOrderWorker(client: ZeebeClient): AbstractWorker(client, "collect-order") {

    override fun work(client: JobClient, job: ActivatedJob): Map<String, Any> {
        Thread.sleep(100)
        return mapOf("orderId" to "1234567890")
    }

}
