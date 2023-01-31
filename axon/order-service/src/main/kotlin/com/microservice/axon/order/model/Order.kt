package com.microservice.axon.order.model

import com.microservice.axon.order.OrderStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    val name: String,
    @Enumerated(EnumType.STRING)
    var status: OrderStatus,
) {

    fun canceled() {
        status = OrderStatus.CANCELED
    }

}