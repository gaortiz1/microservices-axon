package com.microservice.axon.order

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderApp

fun main(args: Array<String>) {
    runApplication<OrderApp>(*args)
}
