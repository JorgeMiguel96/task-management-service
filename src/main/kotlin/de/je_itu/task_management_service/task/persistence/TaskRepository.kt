package de.je_itu.task_management_service.task.persistence

import de.je_itu.task_management_service.shared.types.Taskname
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository: CrudRepository<TaskEntity, Long> {
    fun existsByTaskName(taskName: Taskname): Boolean
}
