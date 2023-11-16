package com.acv.chat.data.openai.chat.chunk

import com.acv.chat.data.openai.FinishReason
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatChunk(
  @SerialName("index") val index: Int,
  @SerialName("delta") val delta: ChatDelta,
  @SerialName("finish_reason") val finishReason: FinishReason?,
)