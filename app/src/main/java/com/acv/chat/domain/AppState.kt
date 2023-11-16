@file:Suppress("FunctionName")

package com.acv.chat.domain

import arrow.optics.optics
import com.acv.chat.components.Theme
import com.acv.chat.domain.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias AppOptics = App.Companion

@optics data class App(
  val screen: Screen? = null,
  val theme: Theme = Theme.Light(),
) : State {
  companion object
}

context(CoroutineScope)
inline fun App(
  crossinline f: suspend context(AppOptics, Store<App>, CoroutineScope) () -> Unit
): Store<App> =
  StoreImpl(App()).also { launch { f(AppOptics, it, this@CoroutineScope) } }