package de.je_itu.task_management_service.task.api

import com.ninjasquad.springmockk.MockkBean
import de.je_itu.task_management_service.shared.errorhandling.TaskNameIsAlreadyExistException
import de.je_itu.task_management_service.shared.errorhandling.TaskNotFoundException
import de.je_itu.task_management_service.task.TestDataProvider.deadline
import de.je_itu.task_management_service.task.TestDataProvider.defaultTaskDTO
import de.je_itu.task_management_service.task.TestDataProvider.defaultTaskId
import de.je_itu.task_management_service.task.TestDataProvider.defaultTaskName
import de.je_itu.task_management_service.task.TestDataProvider.description
import de.je_itu.task_management_service.task.TestDataProvider.jsonPropertyIsWrong
import de.je_itu.task_management_service.task.TestDataProvider.jsonValueIsBlank
import de.je_itu.task_management_service.task.TestDataProvider.jsonValueIsNotValid
import de.je_itu.task_management_service.task.TestDataProvider.status
import de.je_itu.task_management_service.task.TestDataProvider.taskId_2
import de.je_itu.task_management_service.task.TestDataProvider.taskName_2
import de.je_itu.task_management_service.task.business.TaskService
import de.je_itu.task_management_service.task.utils.ObjectMapper.objectMapper
import io.mockk.every
import io.mockk.verify
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(TaskController::class)
@MockkBean(TaskService::class)
class TaskWebMvcTest @Autowired constructor(
    private val taskService: TaskService,
    private val mockMvc: MockMvc
) {
    @Nested
    inner class GetAllTasks {

        @Test
        fun `getting tasks returns 200 OK`() {
            every { taskService.getAllTasks() } returns listOf(
                defaultTaskDTO,
                defaultTaskDTO.copy(id = taskId_2, taskName = taskName_2)
            )

            mockMvc.perform(MockMvcRequestBuilders.get("/task-api/tasks"))
                .andExpectAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.content().json(
                        objectMapper(
                            listOf(
                                defaultTaskDTO,
                                defaultTaskDTO.copy(id = taskId_2, taskName_2)
                            )
                        )
                    )
                )

            verify { taskService.getAllTasks() }
        }

        @Test
        fun `getting tasks returns 500 INTERNAL SERVER ERROR when wrong HTTP method is used`() {
            mockMvc.perform(MockMvcRequestBuilders.post("/task-api/tasks"))
                .andExpectAll(
                    MockMvcResultMatchers.status().isInternalServerError,
                    MockMvcResultMatchers.content().string(StringContains.containsString("An internal error occurred"))
                )
        }

        @Test
        fun `getting tasks returns 500 INTERNAL SERVER ERROR when wrong URL is used`() {
            mockMvc.perform(MockMvcRequestBuilders.get("/task-api/tas"))
                .andExpectAll(
                    MockMvcResultMatchers.status().isInternalServerError,
                    MockMvcResultMatchers.content().string(StringContains.containsString("An internal error occurred"))
                )
        }
    }

    @Nested
    inner class CreateNewTask {

        @Test
        fun `creating a task returns 201 CREATED`() {
            every { taskService.createNewTask(defaultTaskName, description, deadline, status) } returns defaultTaskDTO

            mockMvc.perform(
                MockMvcRequestBuilders.post("/task-api/task")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper(defaultTaskDTO))
            )
                .andExpectAll(
                    MockMvcResultMatchers.status().isCreated,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.content().json(objectMapper(defaultTaskDTO))
                )

            verify { taskService.createNewTask(defaultTaskName, description, deadline, status) }
        }

        @Test
        fun `creating a task returns 400 BAD REQUEST when the JSON property is wrong or missing`() {
            mockMvc.perform(
                MockMvcRequestBuilders.post("/task-api/task")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPropertyIsWrong)
            ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest,
                MockMvcResultMatchers.content()
                    .string(StringContains.containsString(""))
            )
        }

        @Test
        fun `creating a task returns 400 BAD REQUEST when the JSON value is blank`() {
            mockMvc.perform(
                MockMvcRequestBuilders.post("/task-api/task")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonValueIsBlank)
            ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest,
                MockMvcResultMatchers.content()
                    .string(StringContains.containsString("Taskname '' does not match pattern '([a-zA-Z0-9_-]{2,25})( [a-zA-Z0-9]{2,25})*'"))
            )
        }

        @Test
        fun `creating a task returns 400 BAD REQUEST when the JSON value is not valid`() {
            mockMvc.perform(
                MockMvcRequestBuilders.post("/task-api/task")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonValueIsNotValid)
            ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest,
                MockMvcResultMatchers.content()
                    .string(StringContains.containsString("Taskname '$' does not match pattern '([a-zA-Z0-9_-]{2,25})( [a-zA-Z0-9]{2,25})*'"))
            )
        }


        @Test
        fun `creating a task returns 409 CONFLICT when Taskname is already exist`() {
            every { taskService.createNewTask(defaultTaskName, description, deadline, status) } throws TaskNameIsAlreadyExistException()

            mockMvc.perform(
                MockMvcRequestBuilders.post("/task-api/task")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper(defaultTaskDTO))
            ).andExpectAll(
                MockMvcResultMatchers.status().isConflict,
                MockMvcResultMatchers.content()
                    .string(StringContains.containsString("The enter Taskname is already taken."))
            )
        }

        @Test
        fun `creating a task returns 500 INTERNAL SERVER ERROR when wrong HTTP method is used`() {
            mockMvc.perform(MockMvcRequestBuilders.get("/task-api/task"))
                .andExpectAll(
                    MockMvcResultMatchers.status().isInternalServerError,
                    MockMvcResultMatchers.content().string(StringContains.containsString("An internal error occurred"))
                )
        }

        @Test
        fun `creating a task returns 500 INTERNAL SERVER ERROR when wrong URL is used`() {
            mockMvc.perform(MockMvcRequestBuilders.post("/task-api/tas"))
                .andExpectAll(
                    MockMvcResultMatchers.status().isInternalServerError,
                    MockMvcResultMatchers.content().string(StringContains.containsString("An internal error occurred"))
                )
        }
    }

    @Nested
    inner class UpdateTask {

        @Test
        fun `updating a task returns 200 OK`() {
            every { taskService.updateTask(defaultTaskId, defaultTaskName, description, deadline, status) } returns defaultTaskDTO

            mockMvc.perform(
                MockMvcRequestBuilders.put("/task-api/task/$defaultTaskId")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper(defaultTaskDTO))
            )
                .andExpectAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.content().json(objectMapper(defaultTaskDTO))
                )

            verify { taskService.updateTask(defaultTaskId, defaultTaskName, description, deadline, status) }
        }

        @Test
        fun `updating a task returns 404 NOT FOUND when task does not exist`() {
            every { taskService.updateTask(defaultTaskId, defaultTaskName, description, deadline, status) } throws TaskNotFoundException()

            mockMvc.perform(
                MockMvcRequestBuilders.put("/task-api/task/$defaultTaskId")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper(defaultTaskDTO))
            ).andExpectAll(
                MockMvcResultMatchers.status().isNotFound,
                MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("The enter Taskname was not found."))
            )
        }

        @Test
        fun `updating a task returns 409 CONFLICT when Taskname is already taken`() {
            every { taskService.updateTask(defaultTaskId, defaultTaskName, description, deadline, status) } throws TaskNameIsAlreadyExistException()

            mockMvc.perform(
                MockMvcRequestBuilders.put("/task-api/task/$defaultTaskId")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper(defaultTaskDTO))
            ).andExpectAll(
                MockMvcResultMatchers.status().isConflict,
                MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("The enter Taskname is already taken."))
            )
        }

        @Test
        fun `updating a task returns 400 BAD REQUEST when JSON property is missing or wrong`() {
            mockMvc.perform(
                MockMvcRequestBuilders.put("/task-api/task/$defaultTaskId")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPropertyIsWrong)
            ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest
            )
        }

        @Test
        fun `updating a task returns 400 BAD REQUEST when JSON value is blank`() {
            mockMvc.perform(
                MockMvcRequestBuilders.put("/task-api/task/$defaultTaskId")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonValueIsBlank)
            ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest
            )
        }

        @Test
        fun `updating a task returns 400 BAD REQUEST when JSON value is not valid`() {
            mockMvc.perform(
                MockMvcRequestBuilders.put("/task-api/task/$defaultTaskId")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonValueIsNotValid)
            ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest
            )
        }

        @Test
        fun `updating a task returns 500 INTERNAL SERVER ERROR when wrong HTTP method is used`() {
            mockMvc.perform(
                MockMvcRequestBuilders.post("/task-api/task/$defaultTaskId")
            ).andExpectAll(
                MockMvcResultMatchers.status().isInternalServerError,
                MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("An internal error occurred"))
            )
        }

        @Test
        fun `updating a task returns 500 INTERNAL SERVER ERROR when wrong URL is used`() {
            mockMvc.perform(
                MockMvcRequestBuilders.put("/task-api/tas/$defaultTaskId")
            ).andExpectAll(
                MockMvcResultMatchers.status().isInternalServerError,
                MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("An internal error occurred"))
            )
        }
    }

    @Nested
    inner class DeleteTask {

        @Test
        fun `deleting a task returns 200 OK`() {
            every { taskService.deleteTask(defaultTaskId) } returns defaultTaskDTO

            mockMvc.perform(
                MockMvcRequestBuilders.delete("/task-api/task/$defaultTaskId")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(
                MockMvcResultMatchers.status().isOk,
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.content().json(objectMapper(defaultTaskDTO))
            )

            verify { taskService.deleteTask(defaultTaskId) }
        }

        @Test
        fun `deleting a task returns 404 NOT FOUND when task is not found`() {
            every { taskService.deleteTask(defaultTaskId) } throws TaskNotFoundException()

            mockMvc.perform(
                MockMvcRequestBuilders.delete("/task-api/task/$defaultTaskId")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(
                MockMvcResultMatchers.status().isNotFound,
                MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("The enter Taskname was not found."))
            )
        }

        @Test
        fun `deleting a task returns 500 INTERNAL SERVER ERROR when wrong HTTP method is used`() {
            mockMvc.perform(
                MockMvcRequestBuilders.post("/task-api/task/$defaultTaskId")
            ).andExpectAll(
                MockMvcResultMatchers.status().isInternalServerError,
                MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("An internal error occurred"))
            )
        }

        @Test
        fun `deleting a task returns 500 INTERNAL SERVER ERROR when wrong URL is used`() {
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/task-api/tas/$defaultTaskId")
            ).andExpectAll(
                MockMvcResultMatchers.status().isInternalServerError,
                MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("An internal error occurred"))
            )
        }
    }


}