package com.microservice.axon.payment.configuration

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.modelling.saga.repository.SagaStore
import org.axonframework.modelling.saga.repository.jpa.JpaSagaStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AxonConfig {

    @Bean
    fun mySagaStore(entityManagerProvider: EntityManagerProvider): SagaStore<*>? {
        return JpaSagaStore.builder()
            .entityManagerProvider(entityManagerProvider)
            .build()
    }
}