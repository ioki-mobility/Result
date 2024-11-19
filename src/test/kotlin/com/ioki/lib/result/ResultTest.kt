package com.ioki.lib.result

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class ResultTest {
    @Test
    fun `success contains data`() {
        val result = succeedingAction("Success")

        val data = when (result) {
            is Result.Failure -> result.error
            is Result.Success -> result.data
        }

        expectThat(data)
            .isEqualTo("Success")
    }

    @Test
    fun `failure contains error`() {
        val result = failingAction("Failure")

        val data = when (result) {
            is Result.Failure -> result.error
            is Result.Success -> result.data
        }

        expectThat(data)
            .isEqualTo("Failure")
    }

    @Test
    fun `catching() turns exception into Result`() {
        val result = Result.catching {
            throwingAction(IllegalArgumentException())
        }

        expectThat(result)
            .isA<Result.Failure<Exception>>()
            .get { error }
            .assert("error is a IllegalArgumentException") { it is IllegalArgumentException }
    }
}

private fun succeedingAction(result: String): Result<String, String> {
    return result.toSuccess()
}

private fun failingAction(errorMessage: String): Result<String, String> {
    return errorMessage.toFailure()
}

private fun throwingAction(exception: Throwable): String {
    throw exception
}
