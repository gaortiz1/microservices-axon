package com.microservice.axon.payment.aggregate

import com.microservice.axon.payment.ChargePaymentCommand
import com.microservice.axon.payment.PaymentFailedEvent
import com.microservice.axon.payment.PaymentStatus
import com.microservice.axon.payment.PaymentProcessEvent
import com.microservice.axon.payment.PaymentRefundedEvent
import com.microservice.axon.payment.RefundPaymentCommand
import com.microservice.axon.payment.application.request.ChargePaymentRequest
import com.microservice.axon.payment.application.request.RefundPaymentRequest
import com.microservice.axon.payment.application.service.PaymentService
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
import java.util.*

@Aggregate
@Entity
@Table(name = "payment_aggregates")
class PaymentAggregate {

    @Id
    @AggregateIdentifier
    val aggregator_id: String = UUID.randomUUID().toString()
    var paymentId: Int? = null
    var orderId: Int? = null
    var name: String? = null
    @Enumerated(EnumType.STRING)
    var status: PaymentStatus? = null

    constructor()

    @CommandHandler
    constructor(chargePaymentCommand: ChargePaymentCommand, paymentService: PaymentService) {

        log.debug("Executing command {} in payments", chargePaymentCommand)

        val event = try {
            val payment = paymentService.chargePayment(
                ChargePaymentRequest(
                    name = chargePaymentCommand.name,
                    orderId = chargePaymentCommand.orderId,
                )
            )

            PaymentProcessEvent(
                name = payment.name,
                orderId = payment.orderId,
                paymentId = payment.id!!,
                status = payment.status,
            )
        } catch (ex: Exception) {
            PaymentFailedEvent(
                name = chargePaymentCommand.name,
                orderId = chargePaymentCommand.orderId,
                paymentId = null,
                status = PaymentStatus.FAILED,
            )
        }

        apply(event)
    }

    @EventSourcingHandler
    protected fun on(paymentProcessedEvent: PaymentProcessEvent) {
        this.paymentId = paymentProcessedEvent.paymentId
        this.orderId = paymentProcessedEvent.orderId
        this.name = paymentProcessedEvent.name
        this.status = paymentProcessedEvent.status
    }

    @CommandHandler
    fun handle(refundPaymentCommand: RefundPaymentCommand, paymentService: PaymentService) {

        log.debug("Executing command {} in payments", refundPaymentCommand)

        val event = try {
            val payment = paymentService.refundPayment(
                RefundPaymentRequest(
                    paymentId = refundPaymentCommand.paymentId
                )
            )

            PaymentRefundedEvent(
                paymentId = payment.id!!,
                name = payment.name,
                orderId = payment.orderId,
                status = payment.status,
            )
        } catch (ex: Exception) {
            PaymentFailedEvent(
                name = null,
                orderId = null,
                paymentId = refundPaymentCommand.paymentId,
                status = PaymentStatus.FAILED,
            )
        }

        apply(event)
    }

    @EventSourcingHandler
    fun on(paymentCancelledEvent: PaymentRefundedEvent) {
        this.status = paymentCancelledEvent.status
    }

    @EventSourcingHandler
    fun on(paymentFailedEvent: PaymentFailedEvent) {
        this.orderId = paymentFailedEvent.orderId
        this.status = paymentFailedEvent.status
    }

    companion object {
        private val log = LoggerFactory.getLogger(PaymentAggregate::class.java)
    }
}