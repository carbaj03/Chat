package com.acv.chat.data.openai.assistant.runs

import com.acv.chat.data.openai.assistant.Tool
import kotlinx.serialization.Serializable

@Serializable
data class CreateThreadAndRunBetaRequest(
    val assistant_id: String,
    val thread: ThreadObject? = null,
    val model: String? = null,
    val instructions: String? = null,
    val tools: List<Tool>? = null,
    val metadata: Map<String, String>? = null
)

@Serializable
data class ThreadObject(
    val messages: List<InitialMessage>? = null,
    val metadata: Map<String, String>? = null
)

@Serializable
data class InitialMessage(
    val role: String,
    val content: String,
    val file_ids: List<String> = emptyList(),
    val metadata: Map<String, String>? = null
)