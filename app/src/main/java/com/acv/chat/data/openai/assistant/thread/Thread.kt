package com.acv.chat.data.openai.assistant.thread

import com.acv.chat.data.openai.common.Role
import com.acv.chat.data.openai.assistant.file.FileId
import com.acv.chat.data.openai.assistant.runs.ThreadId
import com.acv.chat.data.openai.assistant.thread.Thread
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thread(
  @SerialName("id") val id: ThreadId,
  @SerialName("created_at") val createdAt: Int,
  @SerialName("metadata") val metadata: Map<String, String>? = null
)

@Serializable
data class ThreadRequest(
  @SerialName("messages") val messages: List<ThreadMessage>? = null,
  @SerialName("metadata") val metadata: Map<String, String>? = null,
)

@Serializable
data class ThreadMessage(
  @SerialName("role") val role: Role,
  @SerialName("content") val content: String,
  @SerialName("file_ids") val fileIds: List<FileId>? = null,
  @SerialName("metadata") val metadata: Map<String, String>? = null,
)

operator fun Thread.invoke(block : Thread.() -> Unit) = block(this)