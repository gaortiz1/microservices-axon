package com.microservice.axon.payment.repository

import com.microservice.axon.payment.model.Payment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : CrudRepository<Payment, Int> {
    fun findByOrderId(orderId: Int): Payment?
}