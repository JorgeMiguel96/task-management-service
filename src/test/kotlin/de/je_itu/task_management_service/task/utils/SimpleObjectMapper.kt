package de.je_itu.task_management_service.task.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun simpleObjectMapper(): ObjectMapper = jacksonObjectMapper()

object ObjectMapper {
    fun objectMapper(objectClass: Any): String {
        return jacksonObjectMapper().writeValueAsString(objectClass)
    }
}
