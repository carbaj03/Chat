package com.acv.chat.data.openai.assistant.runs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LastError(
  @SerialName("code") val code: String,
  @SerialName("message") val message: String,
)