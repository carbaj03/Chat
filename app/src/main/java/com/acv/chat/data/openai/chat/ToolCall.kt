package com.acv.chat.data.openai.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToolCall(
    @SerialName("index") val index: Int? = null,
    @SerialName("id") val id: ToolId? = null,
    @SerialName("type") val type: ToolType? = null,
    @SerialName("function") val function: FunctionCall? = null,
)