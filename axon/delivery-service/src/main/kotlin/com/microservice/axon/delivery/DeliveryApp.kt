package com.microservice.axon.delivery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DeliveryApp

fun main(args: Array<String>) {
    runApplication<DeliveryApp>(*args)
}
