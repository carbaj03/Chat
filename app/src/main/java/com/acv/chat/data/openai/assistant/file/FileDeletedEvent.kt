package com.acv.chat.data.openai.assistant.file

import kotlinx.serialization.Serializable

@Serializable
data class FileDeletedEvent(
    val id: String,
    val deleted: Boolean
)