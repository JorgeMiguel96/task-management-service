package de.je_itu.task_management_service.shared

import com.fasterxml.jackson.module.kotlin.readValue
import de.je_itu.task_management_service.shared.types.Taskname
import de.je_itu.task_management_service.task.utils.assertJsonStrictly
import de.je_itu.task_management_service.task.utils.simpleObjectMapper
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class TasknameTests {

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
