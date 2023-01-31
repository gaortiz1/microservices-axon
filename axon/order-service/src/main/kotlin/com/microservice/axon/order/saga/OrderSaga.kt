package com.microservice.axon.order.saga

import com.microservice.axon.order.CancelOrderCommand
import com.microservice.axon.order.OrderCancelledEvent
import com.microservice.axon.order.OrderCreatedEvent
import com.microservice.axon.payment.ChargePaymentCommand
import com.microservice.axon.payment.PaymentFailedEvent
import com.microservice.axon.payment.PaymentProcessEvent
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.send
import org.axonframework.messaging.MetaData
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle.associateWith
import org.axonframework.modelling.saga.SagaLifecycle.end
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

@Saga(sagaStore = "mySagaStore")
class OrderSaga {

    @Autowired
    @Transient
    private lateinit var commandGateway: CommandGateway

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private fun handle(orderCreatedEvent: OrderCreatedEvent) {
        log.debug(
            "OrderCreatedEvent in Saga for Order Id : {}",
            orderCreatedEvent.orderId
        )

        associateWith("orderId", orderCreatedEvent.orderId)

        commandGateway.send(
            command = ChargePaymentCommand(
                name = orderCreatedEvent.name,
                orderId = orderCreatedEvent.orderId,
            ),
            onSuccess = { message: CommandMessage<out ChargePaymentCommand>, result: Any, _: MetaData ->
                log.debug("Successfully handled [{}], resulting in [{}]", message, result)
            },
            onError = { result: Any, exception: Throwable, _: MetaData ->
                log.error(
                    "Failed handling the ChargePaymentCommand, with output [{}]",
                    result, exception
                )
            }
        )
    }

    @SagaEventHandler(associationProperty = "orderId")
    private fun handle(paymentFailedEvent: PaymentFailedEvent) {
        log.debug(
            "PaymentFailedEvent in Saga for Order Id : {}",
            paymentFailedEvent.orderId
        )

        associateWith("orderId", paymentFailedEvent.orderId)

        commandGateway.send(
            command = CancelOrderCommand(
                orderId = paymentFailedEvent.orderId!!,
            ),
            onSuccess = { message: CommandMessage<out CancelOrderCommand>, result: Any, _: MetaData ->
                log.debug("Successfully handled [{}], resulting in [{}]", message, result)
            },
            onError = { result: Any, exception: Throwable, _: MetaData ->
                log.error(
                    "Failed handling the CancelOrderCommand, with output [{}]",
                    result, exception
                )
            }
        )
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    private fun handle(paymentProcessedEvent: PaymentProcessEvent) {
        log.debug(
            "PaymentProcessedEvent in Saga for Order Id : {}",
            paymentProcessedEvent.orderId
        )
        end()
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    private fun handle(orderCancelledEvent: OrderCancelledEvent) {
        log.debug(
            "OrderCancelledEvent in Saga for Order Id : {}",
            orderCancelledEvent.orderId
        )
        end()
    }

    companion object {
        private val log = LoggerFactory.getLogger(OrderSaga::class.java)
    }

}