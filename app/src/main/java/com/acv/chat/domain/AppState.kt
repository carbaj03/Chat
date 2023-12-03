@file:Suppress("FunctionName")

package com.acv.chat.domain

import arrow.optics.optics
import com.acv.chat.Navigator
import com.acv.chat.NavigatorImpl
import com.acv.chat.components.Theme
import com.acv.chat.domain.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias AppOptics = App.Companion

@optics data class App(
  val screen: Screen?,
  val theme: Theme,
) : State {
  companion object
}

context(CoroutineScope)
inline fun App(
//  crossinline f: suspend context(AppOptics, Store<App>, CoroutineScope) () -> Unit
): Store<App> =
  StoreImpl(App(screen = null, theme = Theme.Light()))
//    .also { launch { f(AppOptics, it, this@CoroutineScope) } }