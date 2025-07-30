package de.je_itu.task_management_service.shared

import com.fasterxml.jackson.module.kotlin.readValue
import de.je_itu.task_management_service.shared.types.Taskname
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

class TasknameTests {

    @ParameterizedTest
    @ValueSource(strings = ["Mathe", "Jo", "Aufgabe erfüllen", "Mathe-Hoch2", "Mathe_Hoch2"])
    fun `valid examples`(example: String) {
        assertDoesNotThrow { Taskname(example) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "J", "Aufhhdhdhdhdhhdhddddddddd", "?", "Jorge-Jejj."])
    fun `invalid examples`(example: String) {
        val ex = assertThrows<IllegalArgumentException> { Taskname(example) }
        ex.message shouldBe """Taskname '$example' does not match pattern '([a-zA-ZÄÖÜäöü0-9_-]{2,15})( [a-zA-ZÄÖÜäöü0-9_-]{2,15})*'"""
    }

    @TestFactory
    fun `json serialization`(): List<DynamicTest> {
        val instance = TestClass(Taskname("Tabelle"))
        val json = """{ "taskName": "Tabelle" }"""
        return listOf(
            dynamicTest("serialization") {
                assertJsonStrictly(actual = simpleObjectMapper().writeValueAsString(instance), expected = json)
            },
            dynamicTest("deserialization") {
                simpleObjectMapper().readValue<TestClass>(json) shouldBe instance
            }
        )
    }

    private data class TestClass(val taskName: Taskname)
}
