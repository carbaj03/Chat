package com.acv.chat.arrow.error

import arrow.core.nonFatalOrThrow
import arrow.core.raise.Raise
import arrow.core.raise.RaiseDSL
import arrow.core.raise.effect
import arrow.core.raise.fold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

context(Raise<A>)
@RaiseDSL
@JvmName("catchReified")
inline fun <reified T : Throwable, A> catch(catch: (t: T) -> A, block: () -> A): A =
  arrow.core.raise.catch(block) { t: Throwable -> if (t is T) raise(catch(t)) else throw t }

context(Raise<A>)
inline fun <A, B> catch(onError: (String) -> A, f: () -> B): B =
  try {
    f()
  } catch (t: Throwable) {
    raise(onError(t.nonFatalOrThrow().message.toString()))
  }

//context(CoroutineScope)
//@OptIn(ExperimentalTypeInference::class)
//inline fun <A> onClick(@BuilderInference crossinline block: suspend Raise<A>.() -> Unit): () -> Either<A, Unit> =
//  { fold({ launch { block.invoke(this@fold) } }, { Either.Left(it) }, { Either.Right(Unit) }) }
//
//context(CoroutineScope)
//inline infix fun <A> (() -> Either<A, Unit>).onError(crossinline block: suspend (A) -> Unit): () -> Unit =
//  { invoke().fold(ifLeft = { launch { block(it) } }, ifRight = { }) }
//
//fun <A> (() -> Either<A, Unit>).noError(): () -> Unit =
//  { invoke().fold(ifLeft = { }, ifRight = { }) }

context(CoroutineScope)
inline fun <A, B> launchEffect(
  noinline onError: suspend (A) -> Unit = {},
  crossinline block: suspend Raise<A>.() -> B
): Job =
  launch { effect { block(this) }.fold(recover = onError, transform = {}) }

context(CoroutineScope)
inline fun <A, B> onClick(
  noinline onError: suspend (A) -> Unit = {},
  crossinline action: suspend Raise<A>.() -> B
): () -> Unit = {
  launchEffect(onError) { action(this) }
}

fun interface Action<A, B> {
  operator fun Raise<A>.invoke(): B
}

context(CoroutineScope)
inline fun <A, B, C> action1(
  noinline onError: (A) -> Unit = {},
  crossinline block: suspend Raise<A>.(C) -> B
): (C) -> Unit = { c ->
  launchEffect(onError) { block(this, c) }
}

context(CoroutineScope)
inline fun <A, B, C, D> action2(
  noinline onError: (A) -> Unit = {},
  crossinline block: suspend Raise<A>.(C, D) -> B
): (C, D) -> Unit = { c, d ->
  launchEffect(onError) { block(this, c, d) }
}