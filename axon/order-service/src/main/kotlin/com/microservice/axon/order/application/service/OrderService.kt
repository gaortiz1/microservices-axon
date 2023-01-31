package com.microservice.axon.order.application.service

import com.microservice.axon.exception.NotFoundException
import com.microservice.axon.order.OrderStatus
import com.microservice.axon.order.application.request.CancelOrderRequest
import com.microservice.axon.order.application.request.CreateOrderRequest
import com.microservice.axon.order.model.Order
import com.microservice.axon.order.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {

    fun createOrder(createOrderRequest: CreateOrderRequest): Order {
        return orderRepository.save(
            Order(
                name = createOrderRequest.name,
                status = OrderStatus.CREATED
            )
        )
    }

    fun cancelOrder(cancelOrderRequest: CancelOrderRequest): Order {

        val order = orderRepository.findById(cancelOrderRequest.orderId)
            .orElseThrow { throw NotFoundException("order_not_found") }

        order.canceled()

        return orderRepository.save(order)
    }
}