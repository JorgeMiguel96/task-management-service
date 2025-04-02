package de.je_itu.task_management_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskManagementServiceApplication

fun main(args: Array<String>) {
	runApplication<TaskManagementServiceApplication>(*args)
}
