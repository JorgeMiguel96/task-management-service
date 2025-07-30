package de.je_itu.task_management_service.task.business

import de.je_itu.task_management_service.shared.errorhandling.TaskNameIsAlreadyExistException
import de.je_itu.task_management_service.shared.errorhandling.TaskNotFoundException
import de.je_itu.task_management_service.shared.types.Taskname
import de.je_itu.task_management_service.task.persistence.TaskEntity
import de.je_itu.task_management_service.task.persistence.TaskRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import de.je_itu.task_management_service.task.TestDataProvider.deadline
import de.je_itu.task_management_service.task.TestDataProvider.defaultTaskDTO
import de.je_itu.task_management_service.task.TestDataProvider.defaultTaskEntity
import de.je_itu.task_management_service.task.TestDataProvider.description
import de.je_itu.task_management_service.task.TestDataProvider.status
import de.je_itu.task_management_service.task.TestDataProvider.defaultTaskId
import de.je_itu.task_management_service.task.TestDataProvider.defaultTaskName
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.called
import java.util.Optional

class TaskServiceUnitTest {
    private val taskRepository = mockk<TaskRepository>(relaxed = true)
    private val cut = TaskService(taskRepository)

    @Nested
    inner class GetAllTasks {

        @Test
        fun `getting tasks`() {
            every { taskRepository.findAll() } returns listOf(defaultTaskEntity)

            val result = cut.getAllTasks()

            result shouldBe listOf(defaultTaskDTO)
            verify { taskRepository.findAll() }
        }

        @Test
        fun `getting empty list of tasks`() {
            every { taskRepository.findAll() } returns listOf()

            val result = cut.getAllTasks()

            result shouldBe listOf()
            verify { taskRepository.findAll() }
        }
    }

    @Nested
    inner class CreatingTask {

        @Test
        fun `creating a new task`() {
            val taskCaptor = slot<TaskEntity>()
            every { taskRepository.existsByTaskName(defaultTaskName) } returns false
            every { taskRepository.save(capture(taskCaptor)) } answers { taskCaptor.captured }

            val result = cut.createNewTask(defaultTaskName, description, deadline, status)

            val persistedEntity = taskCaptor.captured
            result shouldBe defaultTaskDTO
            persistedEntity.taskName shouldBe defaultTaskDTO.taskName
            persistedEntity.description shouldBe defaultTaskDTO.description
            persistedEntity.deadline shouldBe defaultTaskDTO.deadline
            persistedEntity.status shouldBe defaultTaskDTO.status
            verify { taskRepository.save(persistedEntity) }
        }

        @Test
        fun `throws a TaskNameIsAlreadyExistException when a Taskname is already taken`() {
            every { taskRepository.existsByTaskName(defaultTaskName) } returns true

            val result = assertThrows<TaskNameIsAlreadyExistException> {
                cut.createNewTask(defaultTaskName, description, deadline, status)
            }

            result.message shouldBe "The enter Taskname is already taken."
            verify { taskRepository.save(any()) wasNot called }
        }
    }

    @Nested
    inner class UpdateTask {

        @Test
        fun `updating a task`() {
            val taskCaptor = slot<TaskEntity>()
            every { taskRepository.findById(defaultTaskId) } returns Optional.of(defaultTaskEntity)
            every { taskRepository.existsByTaskName(defaultTaskName.copy("Task_2")) } returns false
            every { taskRepository.save(capture(taskCaptor)) } answers { taskCaptor.captured }

            val result = cut.updateTask(defaultTaskId, defaultTaskName.copy("Task_2"), description, deadline, status)

            val persistedEntity = taskCaptor.captured
            result shouldBe defaultTaskDTO.copy(taskName = Taskname("Task_2"))
            verify { taskRepository.save(persistedEntity) }
        }

        @Test
        fun `throws a TaskNotFoundException when task does not exist`() {
            every { taskRepository.findById(defaultTaskId) } returns Optional.empty()

            val result = assertThrows<TaskNotFoundException> {
                cut.updateTask(defaultTaskId, defaultTaskName, description, deadline, status)
            }

            result.message shouldBe "The enter Taskname was not found."
            verify { taskRepository.save(any()) wasNot called }
        }

        @Test
        fun `throws a TaskNameIsAlreadyExistException when Taskname is already taken`() {
            every { taskRepository.findById(defaultTaskId) } returns Optional.of(defaultTaskEntity)
            every { taskRepository.existsByTaskName(defaultTaskName.copy("NewTask")) } returns true

            val result = assertThrows<TaskNameIsAlreadyExistException> {
                cut.updateTask(defaultTaskId, defaultTaskName.copy("NewTask"), description, deadline, status)
            }

            result.message shouldBe "The enter Taskname is already taken."
            verify { taskRepository.save(any()) wasNot called }
        }
    }

    @Nested
    inner class DeleteTask {

        @Test
        fun `deleting a task`() {
            every { taskRepository.findById(defaultTaskId) } returns Optional.of(defaultTaskEntity)
            every { taskRepository.deleteById(defaultTaskId) } returns Unit

            val result = cut.deleteTask(defaultTaskId)

            result shouldBe defaultTaskDTO
            verify { taskRepository.deleteById(defaultTaskId) }
        }

        @Test
        fun `throws a TaskNotFoundException when a Task is not found`() {
            every { taskRepository.findById(defaultTaskId) } returns Optional.empty()

            val result = assertThrows<TaskNotFoundException> {
                cut.deleteTask(defaultTaskId)
            }

            result.message shouldBe "The enter Taskname was not found."
            verify { taskRepository.findById(defaultTaskId) }
            verify(exactly = 0) { taskRepository.deleteById(defaultTaskId) }
        }
    }
}
