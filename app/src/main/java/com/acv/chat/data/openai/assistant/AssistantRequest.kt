package com.acv.chat.data.openai.assistant

import com.acv.chat.data.openai.common.ModelId
import com.acv.chat.data.openai.assistant.file.FileId
import com.acv.chat.data.openai.assistant.runs.AssistantId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssistantRequest(
  @SerialName("model") val model: String? = null,
  @SerialName("name") val name: String? = null,
  @SerialName("description") val description: String? = null,
  @SerialName("instructions") val instructions: String? = null,
  @SerialName("tools") val tools: List<AssistantTool>? = null,
  @SerialName("file_ids") val fileIds: List<FileId>? = null,
  @SerialName("metadata") val metadata: Map<String, String>? = null
)

@Serializable
data class Assistant(
  @SerialName("id") val id: AssistantId,
  @SerialName("created_at") val createdAt: Long,
  @SerialName("name") val name: String,
  @SerialName("description") val description: String? = null,
  @SerialName("model") val model: ModelId,
  @SerialName("instructions") val instructions: String? = null,
  @SerialName("tools") val tools: List<AssistantTool>,
  @SerialName("file_ids") val fileIds: List<FileId>,
  @SerialName("metadata") val metadata: Map<String, String>,
)
