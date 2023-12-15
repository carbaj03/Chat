package com.acv.chat.domain

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

interface Analytics {
  fun Event.track(): Unit
}

class FirebaseImpl(context: Context) : Analytics {
  val analytics = FirebaseAnalytics.getInstance(context)

  override fun Event.track() {
    when (this) {
      is Event.Click -> println("Click $text")
      is Event.LongClick -> println("LongClick $text")
      is Event.Screen -> println("Screen $text")
    }
  }
}

class AnalyticsMock : Analytics {
  override fun Event.track() {
    when (this) {
      is Event.Click -> println("Click $text")
      is Event.LongClick -> println("LongClick $text")
      is Event.Screen -> println("Screen $text")
    }
  }
}

sealed interface Event {
  val text: String

  data class Click(override val text: String) : Event
  data class LongClick(override val text: String) : Event
  data class Screen(override val text: String) : Event
}
