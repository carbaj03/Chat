package com.acv.chat.data.openai.assistant.runs

import com.acv.chat.data.openai.assistant.Tool
import kotlinx.serialization.Serializable

@Serializable
data class CreateRunBetaRequest(
    val assistant_id: String,
    val model: String? = null,
    val instructions: String? = null,
    val tools: List<Tool>? = null,
    val metadata: Map<String, String>? = null
)