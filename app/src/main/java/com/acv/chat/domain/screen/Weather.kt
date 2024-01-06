package com.acv.chat.domain.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import arrow.optics.optics
import com.acv.chat.arrow.error.onClick
import com.acv.chat.arrow.optics.invoke
import com.acv.chat.components.ButtonIcon
import com.acv.chat.components.Icon
import com.acv.chat.components.Text
import com.acv.chat.components.TopBar
import com.acv.chat.components.invoke
import com.acv.chat.components.value
import com.acv.chat.domain.App
import com.acv.chat.domain.AppOptics
import com.acv.chat.domain.AssistantWeather
import com.acv.chat.domain.DocumentService
import com.acv.chat.domain.DomainError
import com.acv.chat.domain.Store
import com.acv.chat.domain.screen
import okio.source
import java.io.File

@optics data class Weather(
  val topBar: TopBar,
  val buttonIcon: ButtonIcon,
  val upload: ButtonIcon,
  val weather: Text,
  val error: String? = null,
  override val destroy: () -> Unit
) : Screen {
  companion object
}

context(AppOptics, Store<App>, AssistantWeather, DocumentService)
fun WeatherScreen(): Weather =
  screen.weather {

    Weather(
      topBar = TopBar(
        title = Text("Weather")
      ),
      buttonIcon = ButtonIcon(
        icon = Icon.Translate,
        onClick = onClick(
          action = { weather.value set getWeather("Madrid") },
          onError = {
            if (it is DomainError.UnknownDomainError) weather.value set it.error
            else weather.value set "error"
          }
        )
      ),
      upload = ButtonIcon(
        icon = Icon.Gallery,
        onClick = onClick {
          updateAssistant(File(pickDocument().file).source())
        }
      ),
      weather = Text(""),
      destroy = { }
    )
  }

@Composable
operator fun Weather.invoke() {
  Column {
    buttonIcon()
    upload()
    weather()
  }
}
