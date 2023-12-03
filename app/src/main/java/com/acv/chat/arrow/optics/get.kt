package com.acv.chat.arrow.optics

import arrow.core.raise.Raise
import arrow.optics.Lens
import arrow.optics.Optional
import arrow.optics.typeclasses.Index
import com.acv.chat.domain.DomainError
import com.acv.chat.domain.Store

context(Store<A>, Raise<DomainError>)
@Suppress("NOTHING_TO_INLINE")
inline fun <A, B> Optional<A, B>.get(): B =
  getOrNull(state.value) ?: raise(DomainError.UnknownDomainError("Optional is not defined"))

context(Store<A>, Raise<DomainError>)
fun <A, B> Lens<A, B>.get(): B =
  getOrNull(state.value) ?: raise(DomainError.UnknownDomainError("Lens is not defined"))

operator fun <A, B> Optional<A, List<B>>.get(i: Int): Optional<A, B> =
  compose(Index.list<B>().index(i))

context(Store<A>, Raise<DomainError>)
fun <A, B> Optional<A, List<B>>.last(): Optional<A, B> =
  compose(Index.list<B>().index((get().size - 1)))

context(Store<A>)
fun <A, B> Optional<A, B>.getOrNull(): B? =
  getOrNull(state.value)