package de.je_itu.task_management_service.shared.errorhandling

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(
        TaskNameIsAlreadyExistException::class
    )
    fun handleConflictExceptions(ex: BusinessException) =
        respondWith(CONFLICT, ex.message).also { ex.message }

    @ExceptionHandler(
        HttpMessageNotReadableException::class
    )
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException) =
        respondWith(BAD_REQUEST, ex.cause?.cause?.message).also { ex.message }

    @ExceptionHandler(
        TaskNotFoundException::class
    )
    fun handleNotFoundExceptions(ex: BusinessException) =
            respondWith(NOT_FOUND, ex.message).also { ex.message }

    @ExceptionHandler(
        Exception::class
    )
    fun handleGenericException(ex: Exception) =
        respondWith(INTERNAL_SERVER_ERROR, "An internal error occurred.")
            .also { logger.error(ex.message, ex.javaClass.simpleName) }


    private fun respondWith(responseStatus: HttpStatus, message: String?) =
        ResponseEntity.status(responseStatus).body(message)
}
