package com.acv.chat.data.openai.assistant.runs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToolOutputsRequest(
  @SerialName("tool_outputs") val outputs: List<ToolOutput>
)

@Serializable
data class ToolOutput(
  @SerialName("tool_call_id") val id: String,
  val output: String
)