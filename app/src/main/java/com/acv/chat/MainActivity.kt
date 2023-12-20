package com.acv.chat

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acv.chat.components.Surface
import com.acv.chat.components.Theme
import com.acv.chat.domain.App
import com.acv.chat.domain.screen.Chat
import com.acv.chat.domain.screen.Search2
import com.acv.chat.domain.screen.Settings
import com.acv.chat.ui.theme.invoke

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)

    setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = "chat") {
        composable("chat",
          enterTransition = {
            slideIntoContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
              animationSpec = tween(200)
            )
          },
          exitTransition = {
            slideOutOfContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
              animationSpec = tween(200)
            )
          },
          popEnterTransition = {
            slideIntoContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
              animationSpec = tween(200)
            )
          },
          popExitTransition = {
            slideOutOfContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
              animationSpec = tween(200)
            )
          }
        ) {
          Chat(
            onMenuClick = { navController.navigate("search") },
            onSettingsClick = { navController.navigate("settings") },
          )
        }

        composable(
          route = "search",
          enterTransition = {
            slideIntoContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
              animationSpec = tween(200)
            )
          },
          exitTransition = {
            slideOutOfContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
              animationSpec = tween(200)
            )
          },
          popEnterTransition = {
            slideIntoContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
              animationSpec = tween(200)
            )
          },
          popExitTransition = {
            slideOutOfContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
              animationSpec = tween(200)
            )
          }
        ) {
          Search2(
            onBackClick = { navController.popBackStack() }
          )
        }

        composable(
          route = "settings",
          enterTransition = {
            slideIntoContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
              animationSpec = tween(200)
            )
          },
          exitTransition = {
            slideOutOfContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
              animationSpec = tween(200)
            )
          },
          popEnterTransition = {
            slideIntoContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
              animationSpec = tween(200)
            )
          },
          popExitTransition = {
            slideOutOfContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
              animationSpec = tween(200)
            )
          }

        ) {
          Settings(
            onBackClick = { navController.popBackStack() },
          )
        }
      }
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
//
//          }
//        }
//      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  val appState = App(screen = null, theme = Theme.Light(Surface()))

  appState.theme {
    Greeting("Android")
  }
}