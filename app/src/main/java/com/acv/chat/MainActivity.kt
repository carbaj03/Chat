package com.acv.chat

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.acv.chat.components.Surface
import com.acv.chat.components.Theme
import com.acv.chat.domain.App
import com.acv.chat.domain.screen.Chat
import com.acv.chat.domain.screen.Search2
import com.acv.chat.ui.theme.invoke

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)

    setContent {
      var search by remember { mutableStateOf(false) }
      val navController = rememberNavController()
      Box(Modifier.fillMaxSize()) {

        Chat(navigate = { search = true })
        AnimatedVisibility(search) {
          Search2(onBackClick = { search = false })
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