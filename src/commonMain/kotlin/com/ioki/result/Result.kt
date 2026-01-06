package com.ioki.result

public sealed interface Result<out T, out E> {
    public data class Success<T>(val data: T) : Result<T, Nothing>
    public data class Failure<E>(val error: E) : Result<Nothing, E>

    public companion object {
        public fun <T> catching(block: () -> T): Result<T, Exception> {
            return try {
                block().toSuccess()
            } catch (ex: Exception) {
                ex.toFailure()
            }
        }

        public suspend fun <T> suspendCatching(block: suspend () -> T): Result<T, Exception> {
            return try {
                block().toSuccess()
            } catch (ex: Exception) {
                ex.toFailure()
            }
        }

        public fun <A, B, C, E> zip(
            first: Result<A, E>,
            second: Result<B, E>,
            zipper: (A, B) -> C,
        ): Result<C, E> = first.zipWith(other = second, zipper = zipper)

        public fun <A, B, C, D, E> zip(
            first: Result<A, E>,
            second: Result<B, E>,
            third: Result<C, E>,
            zipper: (A, B, C) -> D,
        ): Result<D, E> = first.flatMap { a ->
            second.flatMap { b ->
                third.map { c -> zipper(a, b, c) }
            }
        }
    }
}
