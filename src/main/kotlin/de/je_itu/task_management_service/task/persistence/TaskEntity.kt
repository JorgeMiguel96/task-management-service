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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq_gen")
    @SequenceGenerator(name = "task_seq_gen", sequenceName = "seq_tasks_id", allocationSize = 1)
    val id: Long = 0L,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "task_name", nullable = false, unique = true))
    var taskName: Taskname,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "description", nullable = false))
    var description: Description,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "deadline", nullable = false))
    var deadline: Deadline,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: Status
)
