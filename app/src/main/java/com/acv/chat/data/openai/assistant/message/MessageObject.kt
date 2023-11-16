package com.acv.chat.data.openai.assistant.message;

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
  @SerialName("id") val id: String,
  @SerialName("object") val `object`: String,
  @SerialName("created_at") val createdAt: Int,
  @SerialName("thread_id") val threadId: String,
  @SerialName("role") val role: String,
  @SerialName("content") val content: List<ContentItem>,
  @SerialName("assistant_id") val assistantId: String?,
  @SerialName("run_id") val runId: String?,
  @SerialName("file_ids") val fileIds: List<String>?,
  @SerialName("metadata") val metadata: Map<String, String>?
)

@Serializable
data class ContentItem(
  val type: String,
  val text: TextContent,
)

@Serializable
data class TextContent(
  val value: String,
  val annotations: List<Annotation> = emptyList()
)