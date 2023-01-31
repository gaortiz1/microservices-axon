package com.microservice.axon.delivery.application.service

import com.microservice.axon.delivery.application.request.CreateDeliveryRequest
import com.microservice.axon.delivery.model.Delivery
import com.microservice.axon.delivery.repository.DeliveryRepository
import org.springframework.stereotype.Service

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository
) {

    fun createOrder(createDeliveryRequest: CreateDeliveryRequest) {
        deliveryRepository.save(
            Delivery(
                address = createDeliveryRequest.address,
                orderId = createDeliveryRequest.orderId,
                paymentId = createDeliveryRequest.paymentId,
                status = "RELEASED"
            )
        )
    }

}