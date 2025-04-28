package de.je_itu.task_management_service.task.persistence

import de.je_itu.task_management_service.shared.types.Deadline
import de.je_itu.task_management_service.shared.types.Description
import de.je_itu.task_management_service.shared.types.Status
import de.je_itu.task_management_service.shared.types.Taskname
import jakarta.persistence.*

@Entity
@Table(name = "tasks")
class TaskEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0L,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "task_name", nullable = false, unique = true))
    val taskName: Taskname,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "description", nullable = false))
    val description: Description,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "deadline", nullable = false))
    val deadline: Deadline,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: Status
)
