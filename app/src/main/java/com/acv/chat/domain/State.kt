package com.acv.chat.domain

import arrow.optics.Copy
import arrow.optics.Setter
import arrow.optics.Traversal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface State

interface Store<A> : Copy<A> {
  val state: StateFlow<A>
  val storeScope: CoroutineScope
}

context(CoroutineScope)
class StoreImpl<A>(
  initialState: A,
) : Store<A> {
  private val _state = MutableStateFlow(initialState)

  override fun <B> Setter<A, B>.set(b: B) {
    _state.value = set(state.value, b)
  }

  override fun <B> Traversal<A, B>.transform(f: (B) -> B) {
    _state.value = modify(state.value, f)
  }

  override val state: StateFlow<A> = _state

  override val storeScope: CoroutineScope = this@CoroutineScope
}