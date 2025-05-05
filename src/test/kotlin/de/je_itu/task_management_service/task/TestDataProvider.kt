package de.je_itu.task_management_service.task

import de.je_itu.task_management_service.shared.types.Deadline
import de.je_itu.task_management_service.shared.types.Description
import de.je_itu.task_management_service.shared.types.Status
import de.je_itu.task_management_service.shared.types.Taskname
import de.je_itu.task_management_service.task.business.model.TaskDTO
import de.je_itu.task_management_service.task.persistence.TaskEntity

object TestDataProvider {
    val defaultTaskId = 0L
    val taskId_2 = 1L
    val defaultTaskName = Taskname("Task_1")
    val taskName_2 = Taskname("Task_2")
    val description = Description("Description_1")
    val deadline = Deadline("2025-07-15")
    val status = Status.ACTIVE

    val defaultTaskEntity = TaskEntity(
        id = defaultTaskId,
        taskName = defaultTaskName,
        description = description,
        deadline = deadline,
        status = status
    )

    val defaultTaskDTO = TaskDTO(
        id = 0L,
        taskName = defaultTaskName,
        description = description,
        deadline = deadline,
        status = status
    )

    val jsonPropertyIsWrong =
        """
           {
                "id": 1,
                "taskNa": "Task_1"
           }
        """

    val jsonValueIsBlank =
        """
           {
                "id": 1,
                "taskName": ""
           }
        """
    val jsonValueIsNotValid =
        """
            {
                "id": 1,
                "taskName": "${'$'}"
            }
        """
}
