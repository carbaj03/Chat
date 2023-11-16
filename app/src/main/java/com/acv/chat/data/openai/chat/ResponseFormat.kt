package com.acv.chat.data.openai.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseFormat(
  @SerialName("type") val type: String
) {

  companion object {
    val JsonObject: ResponseFormat = ResponseFormat(type = "json_object")
    val Text: ResponseFormat = ResponseFormat(type = "text")
  }
}