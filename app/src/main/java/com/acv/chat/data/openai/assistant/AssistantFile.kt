package com.acv.chat.data.openai.assistant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssistantFile(
  @SerialName("id") val id: FileId,
  @SerialName("created_at") val createdAt: Int,
  @SerialName("assistant_id") val assistantId: AssistantId
)

@Serializable
@JvmInline
value class FileId(val id: String)