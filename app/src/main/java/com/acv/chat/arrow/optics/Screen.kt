package com.acv.chat.arrow.optics

import arrow.optics.Optional
import com.acv.chat.domain.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

context(Store<A>)
inline operator fun <A, B> Optional<A, B>.invoke(f: context(CoroutineScope, Optional<A, B>) () -> B): B {
  val scope = CoroutineScope(storeScope.coroutineContext + SupervisorJob())
  return f(scope, this)
}
