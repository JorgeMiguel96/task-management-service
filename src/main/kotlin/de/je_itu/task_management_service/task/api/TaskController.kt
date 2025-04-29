package de.je_itu.task_management_service.task.api

import de.je_itu.task_management_service.task.business.TaskService
import de.je_itu.task_management_service.task.business.model.TaskDTO
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/task-api")
class TaskController (
    private val taskService: TaskService
) {

    @GetMapping("/tasks")
    @ResponseStatus(OK)
    fun getAllTasks(): List<TaskDTO> {
        return taskService.getAllTasks()
    }

    @PostMapping("/task")
    @ResponseStatus(CREATED)
    fun createNewTask(@RequestBody body: TaskRequestBodyDTO): TaskDTO {
        return taskService.createNewTask(body.taskName, body.description, body.deadline, body.status)
    }

    @PutMapping("/task/{taskId}")
    @ResponseStatus(OK)
    fun updateTask(@PathVariable taskId: Long, @RequestBody body: TaskRequestBodyDTO): TaskDTO {
        return taskService.updateTask(taskId, body.taskName, body.description, body.deadline, body.status)
    }
}
