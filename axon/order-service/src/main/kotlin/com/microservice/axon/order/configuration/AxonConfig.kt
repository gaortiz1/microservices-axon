package com.microservice.axon.order.configuration

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.modelling.saga.repository.jpa.JpaSagaStore
import org.axonframework.serialization.Serializer
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableAutoConfiguration
class AxonConfig {

    @Bean("mySagaStore")
    fun mySagaStore(serializer: Serializer, entityManagerProvider: EntityManagerProvider): JpaSagaStore =
        JpaSagaStore
            .builder()
            .entityManagerProvider(entityManagerProvider)
            .serializer(serializer)
            .build()
}