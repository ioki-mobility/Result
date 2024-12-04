package com.ioki.result

public fun <T> T.toSuccess(): Result.Success<T> = Result.Success(this)
public fun <E> E.toFailure(): Result.Failure<E> = Result.Failure(this)

public inline fun <T, R, E> Result<T, E>.map(mapper: (T) -> R): Result<R, E> = when (this) {
    is Result.Failure -> this
    is Result.Success -> Result.Success(mapper(data))
}

public inline fun <T, R, E> Result<T, E>.flatMap(mapper: (T) -> Result<R, E>): Result<R, E> = when (this) {
    is Result.Failure -> this
    is Result.Success -> mapper(data)
}

public inline fun <T, R, E> Result<T, E>.mapFailure(mapper: (E) -> R): Result<T, R> = when (this) {
    is Result.Failure -> Result.Failure(mapper(error))
    is Result.Success -> this
}

public inline fun <T, R, E> Result<T, E>.flatMapFailure(mapper: (E) -> Result<T, R>): Result<T, R> = when (this) {
    is Result.Failure -> mapper(error)
    is Result.Success -> this
}

public inline fun <T, E> Result<T, E>.successOrHandle(handle: E.() -> T): T = when (this) {
    is Result.Failure -> error.handle()
    is Result.Success -> data
}

public inline fun <T, E> Result<T, E>.successOrElse(orElse: () -> T): T = when (this) {
    is Result.Failure -> orElse()
    is Result.Success -> data
}

public fun <T, E> Result<T, E>.successOrNull(): T? = when (this) {
    is Result.Failure -> null
    is Result.Success -> data
}

public fun <T, E> Result<T, E>.failureOrNull(): E? = when (this) {
    is Result.Failure -> error
    is Result.Success -> null
}

public fun <T, E> Result<T, E>.ifFailure(predicate: (E) -> Boolean): Boolean = when (this) {
    is Result.Success -> false
    is Result.Failure -> predicate(this.error)
}

public inline fun <T, E> Result<T, E>.doOnSuccess(block: (T) -> Unit): Result<T, E> = when (this) {
    is Result.Failure -> this
    is Result.Success -> this.also { block(it.data) }
}

public fun <A, B, C, E> Result<A, E>.zipWith(other: Result<B, E>, zipper: (A, B) -> C): Result<C, E> {
    return this.flatMap { a ->
        other.map { b ->
            zipper(a, b)
        }
    }
}
