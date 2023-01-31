package com.microservice.axon.exception

class NotFoundException(
    message: String,
    throwable: Throwable? = null
) : RuntimeException(message, throwable)