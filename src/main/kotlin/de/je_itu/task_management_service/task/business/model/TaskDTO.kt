package de.je_itu.task_management_service.task.business.model

import de.je_itu.task_management_service.shared.types.Deadline
import de.je_itu.task_management_service.shared.types.Description
import de.je_itu.task_management_service.shared.types.Status
import de.je_itu.task_management_service.shared.types.Taskname
import de.je_itu.task_management_service.task.persistence.TaskEntity

data class TaskDTO(
    val id: Long,
    val taskName: Taskname,
    val description: Description,
    val deadline: Deadline,
    val status: Status,
)

fun TaskEntity.toTaskDTO(): TaskDTO {
    return TaskDTO(
        this.id,
        this.taskName,
        this.description,
        this.deadline,
        this.status
    )
}
