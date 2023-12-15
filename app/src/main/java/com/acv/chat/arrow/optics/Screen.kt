package com.acv.chat.arrow.optics

import arrow.optics.Optional
import com.acv.chat.domain.Store
import com.acv.chat.domain.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

context(Store<A>)
inline operator fun <A, B : Screen> Optional<A, B>.invoke(f: context(CoroutineScope, Optional<A, B>) () -> B): B {
  val scope = CoroutineScope(storeScope.coroutineContext + SupervisorJob())
  return f(scope, this)
}


context(Store<A>)
infix inline  fun <A, B> Optional<A, B>.inside(f: Optional<A, B>.() -> Unit) = f(this)