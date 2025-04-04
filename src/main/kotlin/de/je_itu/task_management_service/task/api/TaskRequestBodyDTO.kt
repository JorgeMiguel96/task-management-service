package de.je_itu.task_management_service.task.api

import de.je_itu.task_management_service.shared.types.Deadline
import de.je_itu.task_management_service.shared.types.Description
import de.je_itu.task_management_service.shared.types.Status
import de.je_itu.task_management_service.shared.types.Taskname

data class TaskRequestBodyDTO(
    val taskName: Taskname,
    val description: Description,
    val deadline: Deadline,
    val status: Status
)
