package de.je_itu.task_management_service.shared.types

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.Embeddable
import jakarta.persistence.MappedSuperclass

private val taskNameRegex = Regex("""([a-zA-Z0-9_-]{2,25})( [a-zA-Z0-9]{2,25})*""")

@MappedSuperclass
@Embeddable
data class Taskname @JsonCreator constructor(
    @JsonValue val value: String
) {
    init {
        require(value matches taskNameRegex) {
            "Taskname '$value' does not match pattern '$taskNameRegex'"
        }
    }

    override fun toString() = value
}
