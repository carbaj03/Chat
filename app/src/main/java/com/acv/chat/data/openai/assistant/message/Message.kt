package com.acv.chat.data.openai.assistant.message

import com.acv.chat.data.openai.assistant.file.FileId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
  @SerialName("id") val id: String,
  @SerialName("created_at") val createdAt: Int,
  @SerialName("thread_id") val threadId: String,
  @SerialName("role") val role: String,
  @SerialName("content") val content: List<MessageContent>,
  @SerialName("assistant_id") val assistantId: String?,
  @SerialName("run_id") val runId: String?,
  @SerialName("file_ids") val fileIds: List<String>?,
  @SerialName("metadata") val metadata: Map<String, String>?
)

@Serializable
sealed interface MessageContent {

  @Serializable
  @SerialName("text")
  data class Text(
    @SerialName("text") val text: TextContent
  ) : MessageContent

  @Serializable
  @SerialName("image_file")
  data class Image(
    @SerialName("file_id") val fileId: FileId
  ) : MessageContent

  fun text(): Text? = this as? Text

  fun image(): Image? = this as? Image
}

@Serializable
data class TextContent(
  @SerialName("value") val value: String,
  @SerialName("annotations") val annotations: List<TextAnnotation>
)

@Serializable
sealed interface TextAnnotation {
  val text: String
  val startIndex: Int
  val endIndex: Int
}

@Serializable
@SerialName("file_citation")
data class FileCitationAnnotation(
  @SerialName("text") override val text: String,
  @SerialName("start_index") override val startIndex: Int,
  @SerialName("end_index") override val endIndex: Int,
  @SerialName("file_citation") val fileCitation: FileCitation,
) : TextAnnotation

@Serializable
data class FileCitation(
  @SerialName("file_id") val fileId: FileId,
  @SerialName("quote") val quote: String,
)

@Serializable
@SerialName("file_path")
data class FilePathAnnotation(
  @SerialName("text") override val text: String,
  @SerialName("start_index") override val startIndex: Int,
  @SerialName("end_index") override val endIndex: Int,
  @SerialName("file_path") val filePath: FilePath,
) : TextAnnotation

@Serializable
data class FilePath(
  @SerialName("file_id") val path: FileId
)
