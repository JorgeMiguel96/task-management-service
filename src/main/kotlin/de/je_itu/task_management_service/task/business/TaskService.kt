package de.je_itu.task_management_service.task.business

import de.je_itu.task_management_service.shared.types.Deadline
import de.je_itu.task_management_service.shared.types.Description
import de.je_itu.task_management_service.shared.types.Status
import de.je_itu.task_management_service.shared.types.Taskname
import de.je_itu.task_management_service.task.business.model.TaskDTO
import de.je_itu.task_management_service.task.business.model.toTaskDTO
import de.je_itu.task_management_service.task.persistence.TaskEntity
import de.je_itu.task_management_service.task.persistence.TaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService (
    private val taskRepository: TaskRepository
) {

    fun getAllTasks(): List<TaskDTO> {
        return taskRepository.findAll().map { taskEntity -> taskEntity.toTaskDTO() }
    }

    fun createNewTask(taskName: Taskname, description: Description, deadline: Deadline, status: Status): TaskDTO {
        return taskRepository.save(TaskEntity(taskName = taskName, description = description, deadline = deadline, status = status)).toTaskDTO()
    }
}
