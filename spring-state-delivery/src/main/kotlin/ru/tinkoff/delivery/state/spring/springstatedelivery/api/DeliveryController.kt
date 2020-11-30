package ru.tinkoff.delivery.state.spring.springstatedelivery.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.tinkoff.delivery.state.spring.springstatedelivery.service.DeliveryService

@RestController
class DeliveryController(
    private val deliveryService: DeliveryService
) {

    @GetMapping("/start")
    fun start(@RequestParam businessId: String) = run { deliveryService.start(businessId) }

    @GetMapping("/collect")
    fun collect(@RequestParam businessId: String) = run { deliveryService.collect(businessId) }

    @GetMapping("/deliver")
    fun deliver(@RequestParam businessId: String) = run { deliveryService.deliver(businessId) }

}
