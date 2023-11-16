package com.acv.chat.data.openai.assistant.message

import com.acv.chat.data.openai.assistant.Tool
import kotlinx.serialization.Serializable

@Serializable
data class ModifyAssistantRequest(
    val model: String? = null,
    val name: String? = null,
    val description: String? = null,
    val instructions: String? = null,
    val tools: List<Tool> = emptyList(),
    val file_ids: List<String> = emptyList(),
    val metadata: Map<String, String>? = null
)