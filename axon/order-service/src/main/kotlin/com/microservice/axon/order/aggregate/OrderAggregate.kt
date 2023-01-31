package com.microservice.axon.order.aggregate

import com.microservice.axon.order.CancelOrderCommand
import com.microservice.axon.order.CreateOrderCommand
import com.microservice.axon.order.OrderCancelledEvent
import com.microservice.axon.order.OrderCreatedEvent
import com.microservice.axon.order.OrderStatus
import com.microservice.axon.order.application.request.CancelOrderRequest
import com.microservice.axon.order.application.request.CreateOrderRequest
import com.microservice.axon.order.application.service.OrderService
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import org.slf4j.LoggerFactory
import java.util.UUID

@Aggregate
@Entity
@Table(name = "order_aggregates")
class OrderAggregate {

    @Id
    @AggregateIdentifier
    val aggregator_id: String = UUID.randomUUID().toString()
    var orderId: Int? = null
    var name: String? = null
    @Enumerated(EnumType.STRING)
    var status: OrderStatus? = null

    constructor()

    @CommandHandler
    constructor(createOrderCommand: CreateOrderCommand, orderService: OrderService) {

        log.debug("Executing command {} in order", createOrderCommand)

        val order = orderService.createOrder(
            CreateOrderRequest(
                name = createOrderCommand.name
            )
        )
        apply(
            OrderCreatedEvent(
                orderId = order.id!!,
                name = order.name,
                status = order.status
            )
        )
    }

    @EventSourcingHandler
    fun on(orderCreatedEvent: OrderCreatedEvent) {
        this.orderId = orderCreatedEvent.orderId
        this.name = orderCreatedEvent.name
        this.status = orderCreatedEvent.status
    }

    @CommandHandler
    fun handle(cancelOrderCommand: CancelOrderCommand, orderService: OrderService) {

        log.debug("Executing command {} in order", cancelOrderCommand)

        val order = orderService.cancelOrder(
            CancelOrderRequest(
                orderId = cancelOrderCommand.orderId
            )
        )

        apply(
            OrderCancelledEvent(
                order.id!!,
                order.status
            )
        )
    }

    @EventSourcingHandler
    fun on(orderCancelledEvent: OrderCancelledEvent) {
        this.status = orderCancelledEvent.status
    }

    companion object {
        private val log = LoggerFactory.getLogger(OrderAggregate::class.java)
    }
}