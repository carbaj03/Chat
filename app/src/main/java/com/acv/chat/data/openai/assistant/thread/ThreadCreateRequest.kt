package com.acv.chat.data.openai.assistant.thread

import kotlinx.serialization.Serializable

@Serializable
data class ThreadCreateRequest(
    val messages: List<Message>? = null,
    val metadata: Map<String, String>? = null
)

@Serializable
data class Message(
    val role: String,
    val content: String,
    val file_ids: List<String>? = emptyList(),
    val metadata: Map<String, String>? = null
)