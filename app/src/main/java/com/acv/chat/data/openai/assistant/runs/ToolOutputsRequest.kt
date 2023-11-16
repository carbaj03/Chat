package com.acv.chat.data.openai.assistant.runs

import kotlinx.serialization.Serializable

@Serializable
data class ToolOutputsRequest(
    val tool_outputs: List<ToolOutput>
)

@Serializable
data class ToolOutput(
    val tool_call_id: String,
    val output: String
)