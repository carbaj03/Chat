package com.acv.chat.data.openai.assistant.thread

import com.acv.chat.data.openai.assistant.RoleSerializer
import com.acv.chat.data.openai.assistant.file.FileId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@Serializable(with = RoleSerializer::class)
sealed class Role(val role: String) {
  data object System : Role("system")
  data object User : Role("user")
  data object Assistant : Role("assistant")
  data object Function : Role("function")
  data object Tool : Role("tool")
}