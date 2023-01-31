package com.microservice.axon.payment.application.service

import com.microservice.axon.exception.NotFoundException
import com.microservice.axon.payment.PaymentStatus
import com.microservice.axon.payment.application.request.ChargePaymentRequest
import com.microservice.axon.payment.application.request.RefundPaymentRequest
import com.microservice.axon.payment.model.Payment
import com.microservice.axon.payment.repository.PaymentRepository
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) {

    fun chargePayment(chargePaymentRequest: ChargePaymentRequest): Payment {

        throw IllegalArgumentException("test saga")

        return paymentRepository.save(
            Payment(
                name = chargePaymentRequest.name,
                orderId = chargePaymentRequest.orderId,
                status = PaymentStatus.PROCESS
            )
        )
    }

    fun refundPayment(refundPaymentRequest: RefundPaymentRequest): Payment {

        val payment = paymentRepository.findById(refundPaymentRequest.paymentId)
            .orElseThrow { throw NotFoundException("payment_not_found") }

        payment.refunded()

        return paymentRepository.save(payment)
    }

}