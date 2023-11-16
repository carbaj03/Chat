package com.acv.chat.data.openai.assistant.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageRequest(
    @SerialName("role") val role: String,
    @SerialName("content") val content: String,
    @SerialName("file_ids") val fileIds: List<String> = emptyList(),
    @SerialName("metadata") val metadata: Map<String, String>? = null
)