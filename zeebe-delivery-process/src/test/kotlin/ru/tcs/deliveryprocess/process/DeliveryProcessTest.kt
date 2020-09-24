package ru.tcs.deliveryprocess.process

import io.zeebe.client.ZeebeClient
import io.zeebe.test.ZeebeTestRule
import org.junit.Rule
import org.junit.Test

class DeliveryProcessTest {

    @get:Rule
    val testRule = ZeebeTestRule()

    @Test
    fun test() {
        val client: ZeebeClient = testRule.client
        client
            .newDeployCommand()
            .addResourceFromClasspath("bpmn/delivery-process.bpmn")
            .send()
            .join()
        val workflowInstance = client
            .newCreateInstanceCommand()
            .bpmnProcessId("delivery-process")
            .latestVersion()
            .send()
            .join()

        client
            .newWorker()
            .jobType("collect-order")
            .handler{c,t -> c.newCompleteCommand(t.key).variables(mapOf("orderId" to "1")).send().join()}
            .open()
        client
            .newWorker()
            .jobType("send-order")
            .handler{c,t -> c.newCompleteCommand(t.key).variables(mapOf("packageId" to "1")).send().join()}
            .open()

        client.newPublishMessageCommand()
            .messageName("WaitOfCollectingMessage")
            .correlationKey("1")
            .variables(mapOf("newValue" to "2"))
            .send()

        client.newPublishMessageCommand()
            .messageName("WaitOrderMessage")
            .correlationKey("1")
            .variables(mapOf("newValue" to "2"))
            .send()

        ZeebeTestRule.assertThat(workflowInstance)
            .hasPassed(
                "Start",
                "CollectOrder",
                "WaitOfCollecting",
                "SendOrder",
                "WaitOrder",
                "End"
            )
            .isEnded
    }
}
