package de.je_itu.task_management_service.shared.errorhandling

sealed class BusinessException(
    override val message: String
): RuntimeException()

class TaskNameIsAlreadyExistException: BusinessException(
    "The enter Taskname is already taken."
)

class TaskNotFoundException: BusinessException(
    "The enter Taskname was not found."
)
