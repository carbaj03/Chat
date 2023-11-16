package com.acv.chat.data.openai.chat.chunk

import com.acv.chat.data.openai.chat.ChatRole
import com.acv.chat.data.openai.chat.ToolCall
import com.acv.chat.data.openai.chat.ToolId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatDelta(
  @SerialName("role") val role: ChatRole? = null,
  @SerialName("content") val content: String? = null,
  @SerialName("tool_calls") val toolCalls: List<ToolCall>? = null,
  @SerialName("tool_call_id") val toolCallId: ToolId? = null,
)