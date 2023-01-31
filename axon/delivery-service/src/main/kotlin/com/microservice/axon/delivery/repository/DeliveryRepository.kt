package com.microservice.axon.delivery.repository

import com.microservice.axon.delivery.model.Delivery
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DeliveryRepository : CrudRepository<Delivery, Int>