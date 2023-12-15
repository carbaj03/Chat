package com.acv.chat.data.openai.chat

import com.acv.chat.data.openai.common.Usage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatCompletion(
  @SerialName("id") val id: String,
  @SerialName("created") val created: Int,
  @SerialName("model") val model: String,
  @SerialName("choices") val choices: List<ChatChoice>,
  @SerialName("usage") val usage: Usage? = null,
  @SerialName("system_fingerprint") val systemFingerprint: String? = null,
)
