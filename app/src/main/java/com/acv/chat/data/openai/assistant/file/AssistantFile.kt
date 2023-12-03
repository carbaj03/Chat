package com.acv.chat.data.openai.assistant.file

import com.acv.chat.data.openai.assistant.runs.AssistantId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssistantFile(
  @SerialName("id") val id: FileId,
  @SerialName("created_at") val createdAt: Int,
  @SerialName("assistant_id") val assistantId: AssistantId
)

