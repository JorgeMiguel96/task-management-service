package de.je_itu.task_management_service.shared.types

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.Embeddable
import jakarta.persistence.MappedSuperclass

private val deadlineRegex = Regex("""^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$""")

@MappedSuperclass
@Embeddable
data class Deadline @JsonCreator constructor(
    @JsonValue val value: String
) {
    init {
        require(value matches deadlineRegex) {
            "Deadline '$value' does not match pattern '$deadlineRegex'"
        }
    }

    override fun toString() = value
}
