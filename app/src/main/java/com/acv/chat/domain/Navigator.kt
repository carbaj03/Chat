package com.acv.chat.domain

import com.acv.chat.domain.screen.AssistantsScreen
import com.acv.chat.domain.screen.HomeScreen
import com.acv.chat.domain.screen.WeatherScreen
import with

interface Navigator {
  fun toWeather()
  fun toHome()
  fun toAssistants()
}

context(Dependencies)
class NavigatorImpl : Navigator {

  override fun toWeather() {
    with(optics, store, assistantWeather, documentService) {
      screen set WeatherScreen()
    }
  }

  override fun toHome() {
    with(optics, store, analytics, photoService, mediaPicker, documentService, audioPlayer, audioRecorder, chat, audio) {
      screen set HomeScreen()
    }
  }

  override fun toAssistants() {
    with(optics, store, assistantApi) {
      screen set AssistantsScreen()
    }
  }
}