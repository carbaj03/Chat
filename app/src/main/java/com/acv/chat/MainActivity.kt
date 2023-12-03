package com.acv.chat

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.acv.chat.components.Surface
import com.acv.chat.components.Theme
import com.acv.chat.components.invoke
import com.acv.chat.domain.App
import com.acv.chat.domain.createDependencies
import com.acv.chat.domain.screen
import com.acv.chat.domain.screen.Home
import com.acv.chat.domain.screen.HomeScreen
import com.acv.chat.domain.screen.Weather
import com.acv.chat.domain.screen.WeatherScreen
import com.acv.chat.domain.screen.invoke
import com.acv.chat.ui.theme.invoke
import with

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)

    setContent {
      val app = createDependencies()
      val navigator = with(app) { NavigatorImpl() }
      val state by app.store.state.collectAsState()

      state.theme {
        state.theme.surface(modifier = Modifier.fillMaxSize()) {
          when (val Screen = state.screen) {
            null -> { navigator.toHome() }
            is Home -> Screen()
            is Weather -> Screen()
          }
        }
      }
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



interface Navigator {
  fun navigateToWeather()

  fun toHome()
}

context(com.acv.chat.domain.Dependencies)
class NavigatorImpl() : Navigator {

  override fun navigateToWeather() {
    with(optics, store, assistantWeather, documentService) {
      screen set WeatherScreen()
    }
  }

  override fun toHome() {
    with(optics, store, photoService, mediaPicker, documentService, audioPlayer, audioRecorder, chat, audio) {
      screen set HomeScreen()
    }
  }
}