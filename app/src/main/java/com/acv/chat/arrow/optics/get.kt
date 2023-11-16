package com.acv.chat.arrow.optics

import arrow.core.raise.Raise
import arrow.optics.Lens
import arrow.optics.Optional
import arrow.optics.typeclasses.Index
import com.acv.chat.domain.Store
import com.acv.chat.domain.DomainError

context(Store<A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <A, B> Optional<A, B>.get(): B =
  getOrNull(state.value) ?: throw Exception("Optional is not defined")

context(Store<A>, Raise<DomainError>)
fun <A, B> Lens<A, B>.get(): B =
  getOrNull(state.value) ?: raise(DomainError.UnknownDomainError("Lens is not defined"))

operator fun <T, A> Optional<T, List<A>>.get(i: Int): Optional<T, A> =
  this.compose(Index.list<A>().index(i))

context(Store<A>)
fun <A, B> Optional<A, B>.getOrNull(): B? =
  getOrNull(state.value)