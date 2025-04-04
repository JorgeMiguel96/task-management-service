package de.je_itu.task_management_service.shared.types

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.Embeddable
import jakarta.persistence.MappedSuperclass

private val descriptionRegex = Regex("""([a-zA-Z0-9_-]{2,25})( [a-zA-Z0-9]{2,25})*""")

@MappedSuperclass
@Embeddable
data class Description @JsonCreator constructor(
    @JsonValue val value: String
) {
    init {
        require(value matches descriptionRegex) {
            "Description '$value' does not match pattern '$descriptionRegex'"
        }
    }

    override fun toString() = value
}
