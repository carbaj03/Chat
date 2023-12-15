package com.acv.chat.data.openai.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usage(
  @SerialName("prompt_tokens") val promptTokens: Int? = null,
  @SerialName("completion_tokens") val completionTokens: Int? = null,
  @SerialName("total_tokens") val totalTokens: Int? = null,
)