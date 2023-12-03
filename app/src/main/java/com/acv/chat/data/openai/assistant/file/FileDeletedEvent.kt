package com.acv.chat.data.openai.assistant.file

import kotlinx.serialization.Serializable

@Serializable
data class FileDeletedEvent(
    val id: String,
    val `object`: String, // Using backticks to escape the reserved keyword 'object'
    val deleted: Boolean
)