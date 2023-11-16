package com.acv.chat.data.openai.chat.chunk

import com.acv.chat.data.openai.Usage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatCompletionChunk(
  @SerialName("id") val id: String,
  @SerialName("created") val created: Int,
  @SerialName("model") val model: String,
  @SerialName("choices") val choices: List<ChatChunk>,
  @SerialName("usage") val usage: Usage? = null,
)
