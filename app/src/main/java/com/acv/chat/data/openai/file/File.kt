package com.acv.chat.data.openai.file

import com.acv.chat.data.openai.assistant.file.FileId
import com.acv.chat.data.openai.assistant.file.Purpose
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class File(
  @SerialName("id") val id: FileId,
  @SerialName("bytes") val bytes: Int,
  @SerialName("created_at") val createdAt: Long,
  @SerialName("filename") val filename: String,
  @SerialName("purpose") val purpose: Purpose,
)

@Serializable
data class FilesResponse(
  @SerialName("data") val files: List<File>
)