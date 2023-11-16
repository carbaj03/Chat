package com.acv.chat.arrow.error

import android.util.Log
import arrow.core.raise.Raise
import arrow.core.raise.effect
import arrow.core.raise.fold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

context(Raise<A>)
inline fun <A, B> catch(onError: (String) -> A, f: () -> B): B =
  try {
    f()
  } catch (t: Throwable) {
    raise(onError(t.message.toString().also { Log.e("Error", t.message.toString())}))
  }

context(CoroutineScope)
inline fun <A, B> launchEffect(
  noinline onError: (A) -> Unit = {},
  crossinline block: suspend Raise<A>.() -> B
): Job =
  launch { effect { block(this) }.fold(recover = onError, {}) }

context(CoroutineScope)
inline fun <A, B> action(
  noinline onError: (A) -> Unit = {},
  crossinline block: suspend Raise<A>.() -> B
): () -> Unit = {
  launchEffect(onError) { block(this) }
}

context(CoroutineScope)
inline fun <A, B, C> action1(
  noinline onError: (A) -> Unit = {},
  crossinline block: suspend Raise<A>.(C) -> B
): (C) -> Unit = { c ->
  launchEffect(onError) { block(this, c) }
}

context(CoroutineScope)
inline fun <A, B, C, D> onClick2(
  noinline onError: (A) -> Unit = {},
  crossinline block: suspend Raise<A>.(C, D) -> B
): (C, D) -> Unit = { c, d ->
  launchEffect(onError) { block(this, c, d) }
}