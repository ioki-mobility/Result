package com.ioki.result

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull

class ResultExtTest {
    @Test
    fun `map() maps success value`() {
        val result = succeedingAction("I like Java")
            .map { it.replace("Java", "Kotlin") }

        expectThat(result)
            .isA<Result.Success<String>>()
            .get { data }
            .isEqualTo("I like Kotlin")
    }

    @Test
    fun `map() keeps failure and does nothing`() {
        val result = failingAction("Stop!")
            .map { 42 }

        expectThat(result)
            .isA<Result.Failure<String>>()
            .get { error }
            .isEqualTo("Stop!")
    }

    @Test
    fun `flatMap() maps success`() {
        val result = succeedingAction("42")
            .flatMap { Result.catching { it.toInt() } }

        expectThat(result)
            .isA<Result.Success<Int>>()
            .get { data }
            .isEqualTo(42)
    }

    @Test
    fun `flatMap() returns Result Failure when mapper returns Failure`() {
        val result = succeedingAction("I like Rust")
            .flatMap { Result.catching { it.toInt() } }

        expectThat(result)
            .isA<Result.Failure<Throwable>>()
            .get { error }
            .assert("error is a NumberFormatException") { it is NumberFormatException }
    }

    @Test
    fun `mapFailure() maps a failure`() {
        val result = failingAction("IO Failure")
            .mapFailure { if (it.contains("IO")) Error.IO else Error.Generic }

        expectThat(result)
            .isA<Result.Failure<Error>>()
            .get { error }
            .assert("error is a IO Error") { it is Error.IO }
    }

    @Test
    fun `mapFailure() keeps success as is`() {
        val result = succeedingAction("All Good!")
            .mapFailure { if (it.contains("IO")) Error.IO else Error.Generic }

        expectThat(result)
            .isA<Result.Success<String>>()
            .get { data }
            .isEqualTo("All Good!")
    }

    @Test
    fun `successOrHandle unwraps Success data`() {
        val data = succeedingAction("Data")
            .successOrHandle { "Error" }

        expectThat(data)
            .isEqualTo("Data")
    }

    @Test
    fun `successOrHandle handles error`() {
        val data = failingAction(Error.IO)
            .successOrHandle {
                when (this) {
                    is Error.IO -> "IO"
                    is Error.Generic -> "Generic"
                }
            }

        expectThat(data)
            .isEqualTo("IO")
    }

    @Test
    fun `successOrElse unwraps Success data`() {
        val data = succeedingAction("Data")
            .successOrElse { "Error" }

        expectThat(data)
            .isEqualTo("Data")
    }

    @Test
    fun `successOrElse returns else on error`() {
        val data = failingAction(Error.IO)
            .successOrElse { "Error!" }

        expectThat(data)
            .isEqualTo("Error!")
    }

    @Test
    fun `successOrNull unwraps Success data`() {
        val data = succeedingAction("Data")
            .successOrNull()

        expectThat(data)
            .isNotNull()
            .isEqualTo("Data")
    }

    @Test
    fun `successOrNull returns null on error`() {
        val data = failingAction(Error.IO)
            .successOrNull()

        expectThat(data)
            .isNull()
    }
}

private fun succeedingAction(result: String): Result<String, String> {
    return result.toSuccess()
}

private fun failingAction(errorMessage: String): Result<String, String> {
    return errorMessage.toFailure()
}

private fun failingAction(error: Error): Result<String, Error> {
    return error.toFailure()
}

private sealed interface Error {
    data object IO : Error
    data object Generic : Error
}
