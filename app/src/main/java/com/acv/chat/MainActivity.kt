package com.acv.chat

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acv.chat.components.Surface
import com.acv.chat.components.Theme
import com.acv.chat.domain.App
import com.acv.chat.ui.theme.invoke

//      val di = rememberDependencies()
//      val navigator = remember { with(di) { NavigatorImpl() } }
//
//      val state by di.store.state.collectAsState()
//
//      state.theme {
//        state.theme.surface(modifier = Modifier.fillMaxSize()) {
//          when (val Screen = state.screen) {
//            null -> {
//              LaunchedEffect(Unit) {
//                navigator.toWeather()
//              }
//            }
//            is Home -> Screen()
//            is Weather -> Screen()
//            is Assistants -> Screen()
//          }
//        }
//      }

val enterSlideLeft: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
  slideIntoContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
    animationSpec = tween(200)
  )
}

val exitSlideLeft: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
  slideOutOfContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
    animationSpec = tween(200)
  )
}

val popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
  slideIntoContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
    animationSpec = tween(200)
  )
}

val popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
  slideOutOfContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
    animationSpec = tween(200)
  )
}

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)

    setContent {

      var theme: Theme by remember { mutableStateOf(Theme.Dark(Surface())) }

      DisposableEffect(theme) {
        enableEdgeToEdge(
          statusBarStyle = when (theme) {
            is Theme.Dark -> SystemBarStyle.dark(Color.TRANSPARENT)
            is Theme.Light -> SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
          },
          navigationBarStyle = when (theme) {
            is Theme.Dark -> SystemBarStyle.dark(Color.TRANSPARENT)
            is Theme.Light -> SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
          },
        )
        onDispose {}
      }

      theme {
        val navController = rememberNavController()

        NavHost(
          navController = navController,
          startDestination = "chat"
        ) {
          chat(
            onMenuClick = { navController.navigate("search") },
            onSettingsClick = { navController.navigate("settings") },
          )
          search(
            onBackClick = { navController.popBackStack() },
          )
          settings(
            onBackClick = { navController.popBackStack() },
            onThemeClick = {
              theme = when (it) {
                com.acv.chat.Theme.Default -> Theme.Light(Surface())
                com.acv.chat.Theme.Light -> Theme.Light(Surface())
                com.acv.chat.Theme.Dark -> Theme.Dark(Surface(androidx.compose.ui.graphics.Color.Black))
              }
            },
            themeSelected = when (theme) {
              is Theme.Dark -> com.acv.chat.Theme.Dark
              is Theme.Light -> com.acv.chat.Theme.Light
            },
          )
        }
      }
    }
  }
}

fun NavGraphBuilder.settings(
  onBackClick: () -> Unit,
  onThemeClick: (com.acv.chat.Theme) -> Unit,
  themeSelected: com.acv.chat.Theme
) {
  composable(
    route = "settings",
    enterTransition = enterSlideLeft,
    exitTransition = exitSlideLeft,
  ) {
    Settings(
      onBackClick = onBackClick,
      themeSelected = themeSelected,
      onThemeClick = onThemeClick
    )
  }
}

fun NavGraphBuilder.chat(
  onMenuClick: () -> Unit,
  onSettingsClick: () -> Unit
) {
  composable(
    route = "chat",
    enterTransition = enterSlideLeft,
    exitTransition = exitSlideLeft,
    popEnterTransition = popEnterTransition,
    popExitTransition = popExitTransition,
  ) {
    Chat(
      onMenuClick = onMenuClick,
      onSettingsClick = onSettingsClick,
    )
  }
}

fun NavGraphBuilder.search(
  onBackClick: () -> Unit,
) {
  composable(
    route = "search",
    enterTransition = enterSlideLeft,
    exitTransition = exitSlideLeft,
  ) {
    SearchScreen(
      onBackClick = onBackClick
    )
  }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  val appState = App(screen = null, theme = Theme.Light(Surface()))

  appState.theme {

  }
}