package com.microservice.axon.payment

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ChargePaymentCommand(
    @TargetAggregateIdentifier
    val orderId: Int,
    val name: String
)

data class RefundPaymentCommand(
    @TargetAggregateIdentifier
    val paymentId: Int,
)