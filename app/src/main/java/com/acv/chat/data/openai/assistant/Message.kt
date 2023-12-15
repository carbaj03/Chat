package com.acv.chat.data.openai.assistant;

import com.acv.chat.data.openai.common.Role
import com.acv.chat.data.openai.assistant.file.FileId
import com.acv.chat.data.openai.assistant.runs.ThreadId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThreadMessage(
  @SerialName("id") val id: MessageId,
  @SerialName("created_at") val createdAt: Int,
  @SerialName("thread_id") val threadId: ThreadId,
  @SerialName("role") val role: Role,
  @SerialName("content") val content: List<MessageContent>,
  @SerialName("assistant_id") val assistantId: String? = null,
  @SerialName("run_id") val runId: String? = null,
  @SerialName("file_ids") val fileIds: List<FileId>,
  @SerialName("metadata") val metadata: Map<String, String>
)

@Serializable
@JvmInline
value class MessageId(val id: String)


@Serializable(with = MessageContentSerializer::class)
sealed interface MessageContent

@Serializable
data class MessageTextContent(@SerialName("text") val text: String) : MessageContent

data class TextContent(
  @SerialName("value") val value: String,
  @SerialName("annotations") val annotations: List<TextAnnotation>
)

@Serializable(with = TextAnnotationSerializer::class)
sealed interface TextAnnotation

@Serializable
data class FileCitationAnnotation(
  @SerialName("file_citation") val fileCitation: FileCitation,
  @SerialName("start_index") val startIndex: Int,
  @SerialName("end_index") val endIndex: Int
) : TextAnnotation

@Serializable
data class FileCitation(
  @SerialName("file") val fileId: FileId,
  @SerialName("quote") val quote: String,
)

@Serializable
data class FilePathAnnotation(
  @SerialName("text") val text: String,
  @SerialName("file_path") val filePath: FilePath,
  @SerialName("start_index") val startIndex: Int,
  @SerialName("end_index") val endIndex: Int
) : TextAnnotation

@Serializable
data class FilePath(
  @SerialName("path") val path: String
)

@Serializable
data class MessageImageContent(
  @SerialName("file_id") val fileId: FileId
) : MessageContent