package com.ioki.result

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

    @Test
    fun `zip() with two Results return zipper`() {
        val result = Result.zip(
            succeedingAction("Success"),
            succeedingAction("Again Success"),
        ) { a, b -> "$a & $b" }

        expectThat(result)
            .isA<Result.Success<String>>()
            .get { data }
            .isEqualTo("Success & Again Success")
    }

    @Test
    fun `zip() with three Results return zipper`() {
        val result = Result.zip(
            succeedingAction("Success"),
            succeedingAction("Again Success"),
            succeedingAction("And Again Success"),
        ) { a, b, c -> "$a & $b & $c" }

        expectThat(result)
            .isA<Result.Success<String>>()
            .get { data }
            .isEqualTo("Success & Again Success & And Again Success")
    }

    @Test
    fun `zip() with three Results return first failure`() {
        val result = Result.zip(
            failingAction("Failure"),
            succeedingAction("Again Success"),
            succeedingAction("And Again Success"),
        ) { a, b, c -> "$a & $b & $c" }

        expectThat(result)
            .isA<Result.Failure<String>>()
            .get { error }
            .isEqualTo("Failure")
    }

    @Test
    fun `zip() with three Results return second failure`() {
        val result = Result.zip(
            succeedingAction("Success"),
            failingAction("Failure"),
            succeedingAction("And Again Success"),
        ) { a, b, c -> "$a & $b & $c" }

        expectThat(result)
            .isA<Result.Failure<String>>()
            .get { error }
            .isEqualTo("Failure")
    }

    @Test
    fun `zip() with three Results return third failure`() {
        val result = Result.zip(
            succeedingAction("Success"),
            succeedingAction("Again Success"),
            failingAction("Failure"),
        ) { a, b, c -> "$a & $b & $c" }

        expectThat(result)
            .isA<Result.Failure<String>>()
            .get { error }
            .isEqualTo("Failure")
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
