package com.acv.chat.data.openai.audio

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslationOAI(
  @SerialName("text") val text: String,
  @SerialName("language") val language: String? = null,
  @SerialName("duration") val duration: Double? = null,
  @SerialName("segments") val segments: List<Segment>? = null,
)
