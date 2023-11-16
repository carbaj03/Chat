package com.acv.chat.data.openai.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatResponseFormat(
  @SerialName("type") val type: String
) {

  companion object {
    val JsonObject: ChatResponseFormat = ChatResponseFormat(type = "json_object")
    val Text: ChatResponseFormat = ChatResponseFormat(type = "text")
  }
}