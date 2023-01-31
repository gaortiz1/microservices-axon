package com.microservice.axon.order

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

data class CreateOrderCommand(
    @TargetAggregateIdentifier
    val name: String,
)

data class CancelOrderCommand(
    @TargetAggregateIdentifier
    val orderId: Int,
)