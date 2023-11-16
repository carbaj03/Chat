package com.acv.chat.data.openai.assistant.thread

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThreadResponse(
  @SerialName("id") val id: String,
  @SerialName("object") val `object`: String,
  @SerialName("created_at") val createdAt: Int,
  @SerialName("metadata") val metadata: Map<String, String>? = null
)