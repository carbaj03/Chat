package com.acv.chat.data.openai.chat

import com.acv.chat.data.openai.common.Parameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatCompletionFunction(
  @SerialName("name") val name: String,
  @SerialName("description") val description: String? = null,
  @SerialName("parameters") val parameters: Parameters,
)
