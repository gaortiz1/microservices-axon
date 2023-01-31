package com.microservice.axon.payment.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration


@Configuration
@EnableAutoConfiguration
class AxonConfig {

    /*
    @Bean
    fun xStream(): XStream {
        val xStream = XStream()
        xStream.allowTypesByWildcard(
            arrayOf(
                "com.microservice.axon.**"
            )
        )
        return xStream
    }

     */
}