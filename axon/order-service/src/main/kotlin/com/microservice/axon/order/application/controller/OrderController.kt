package com.microservice.axon.order.application.controller

import com.microservice.axon.order.CreateOrderCommand
import com.microservice.axon.order.application.request.CreateOrderRequest
import com.microservice.axon.order.saga.OrderSaga
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.send
import org.axonframework.messaging.MetaData
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class OrderController(
    private val commandGateway: CommandGateway,
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody createOrderRequest: CreateOrderRequest) {

        commandGateway.send(
            command = CreateOrderCommand(
                name = createOrderRequest.name,
            ),
            onSuccess = { message: CommandMessage<out CreateOrderCommand>, result: Any, _: MetaData ->
                log.info("Successfully handled [{}], resulting in [{}]", message, result)
            },
            onError = { result: Any, exception: Throwable, _: MetaData ->
                log.error(
                    "Failed handling the CreateOrderCommand, with output {}",
                    result, exception
                )
            }
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(OrderSaga::class.java)
    }
}