package ru.tinkoff.delivery.state.spring.springstatedelivery.service

interface DeliveryService {

    fun start(businessId: String)

    fun deliver(businessId: String)

    fun collect(businessId: String)

}
