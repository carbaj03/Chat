package com.acv.chat.data.openai.chat

import com.acv.chat.data.openai.common.FinishReason
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatChoice(
  @SerialName("index") val index: Int,
  @SerialName("message") val message: ChatMessage,
  @SerialName("finish_reason") val finishReason: FinishReason? = null,
)
