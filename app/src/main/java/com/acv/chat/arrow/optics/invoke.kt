package com.acv.chat.arrow.optics

import arrow.core.raise.Raise
import arrow.optics.Optional
import com.acv.chat.domain.DomainError
import com.acv.chat.domain.Store

context(Store<A>, Raise<DomainError>)
operator fun <A> Optional<A, () -> Unit>.invoke() {
  getOrNull(state.value)?.invoke()
}