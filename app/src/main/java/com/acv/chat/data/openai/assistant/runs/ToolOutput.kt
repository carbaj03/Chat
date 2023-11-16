package com.acv.chat.data.openai.assistant.runs

import kotlinx.serialization.Serializable

@Serializable
data class RequestBody(
    val tool_outputs: List<ToolOutput>
)