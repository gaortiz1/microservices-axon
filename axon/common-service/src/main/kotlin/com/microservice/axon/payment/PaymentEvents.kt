package com.microservice.axon.payment

data class PaymentProcessEvent(
    val paymentId: Int,
    val orderId: Int,
    val name: String,
    var status: PaymentStatus,
)

data class PaymentFailedEvent(
    val paymentId: Int?,
    val orderId: Int?,
    val name: String?,
    var status: PaymentStatus,
)

data class PaymentRefundedEvent(
    val paymentId: Int,
    val orderId: Int,
    val name: String,
    var status: PaymentStatus,
)