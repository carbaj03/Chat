package com.acv.chat.data.openai.assistant.runs

import com.acv.chat.data.openai.ModelId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RunRequest(
  @SerialName("assistant_id") val assistantId: AssistantId,
  @SerialName("model") val model: ModelId? = null,
  @SerialName("instructions") val instructions: String? = null,
  @SerialName("tools") val tools: List<AssistantTool>? = null,
  @SerialName("metadata") val metadata: Map<String, String>? = null,
)
