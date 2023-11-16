package com.acv.chat.data.openai.assistant;

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThreadMessage(
  @SerialName("id") val id: String,
  @SerialName("object") val `object`: String,
  @SerialName("created_at") val createdAt: Int,
  @SerialName("thread_id") val threadId: String,
  @SerialName("role") val role: String,
  @SerialName("content") val content: List<String>,
  @SerialName("assistant_id") val assistantId: String?,
  @SerialName("run_id") val runId: String?,
  @SerialName("file_ids") val fileIds: List<String>?,
  @SerialName("metadata") val metadata: Map<String, String>?
) 