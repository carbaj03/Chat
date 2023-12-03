package com.acv.chat.data.openai.assistant.thread

import com.acv.chat.data.openai.assistant.runs.ThreadId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThreadResponse(
  @SerialName("id") val id: ThreadId,
  @SerialName("created_at") val createdAt: Int,
  @SerialName("metadata") val metadata: Map<String, String>? = null
)