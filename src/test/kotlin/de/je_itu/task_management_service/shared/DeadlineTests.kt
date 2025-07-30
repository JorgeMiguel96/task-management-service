package de.je_itu.task_management_service.shared

import com.fasterxml.jackson.module.kotlin.readValue
import de.je_itu.task_management_service.shared.types.Deadline
import de.je_itu.task_management_service.task.utils.assertJsonStrictly
import de.je_itu.task_management_service.task.utils.simpleObjectMapper
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DeadlineTests {

    @ParameterizedTest
    @ValueSource(strings = ["2025-04-04"])
    fun `valid examples`(example: String) {
        assertDoesNotThrow { Deadline(example) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "2025.04.04", "2025/04/04", "04.04.2025", "?"])
    fun `invalid example`(example: String) {
        val ex = assertThrows<IllegalArgumentException> { Deadline(example) }
        ex.message shouldBe """Deadline '$example' does not match pattern: ^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$"""
    }

    @TestFactory
    fun `json serialization`(): List<DynamicTest> {
        val instance = TestClass(Deadline("2025-04-04"))
        val json = """{ "deadline": "2025-04-04" }"""
        return listOf(
            dynamicTest("serialization") {
                assertJsonStrictly(actual = simpleObjectMapper().writeValueAsString(instance), expected = json)
            },
            dynamicTest("deserialization") {
                simpleObjectMapper().readValue<TestClass>(json) shouldBe instance
            }
        )
    }

    private data class TestClass(val deadline: Deadline)
}
