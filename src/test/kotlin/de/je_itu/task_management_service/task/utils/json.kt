package de.je_itu.task_management_service.task.utils

import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.skyscreamer.jsonassert.JSONAssert

fun assertJsonStrictly(@Language("json") actual: String?, @Language("json") expected: String?) {
    when {
        actual == null || expected == null -> assertThat(actual).isEqualTo(expected)
        else -> try {
            JSONAssert.assertEquals(expected, actual, /* strict */ true)
        } catch (e: AssertionError) {
            println("actual JSON: $actual")
            throw e
        }
    }
}
