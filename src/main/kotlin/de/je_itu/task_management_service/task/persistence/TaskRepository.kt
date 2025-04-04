package de.je_itu.task_management_service.task.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository: CrudRepository<TaskEntity, Long>
