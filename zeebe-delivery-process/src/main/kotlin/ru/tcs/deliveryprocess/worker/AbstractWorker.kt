package ru.tcs.deliveryprocess.worker

import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.response.ActivatedJob
import io.zeebe.client.api.worker.JobClient
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

abstract class AbstractWorker(client: ZeebeClient, workerType: String) {

    init {
        client.newWorker()
            .jobType(workerType)
            .handler { cl, job ->
                logger.info("Worker $workerType starts!")
                val result = work(cl, job)
                cl.newCompleteCommand(job.key)
                    .variables(result)
                    .send()
                    .join()
                logger.info("Worker $workerType finishes!")
            }
        .open()
    }

    abstract fun work(client: JobClient, job: ActivatedJob): Map<String, Any?>
}
