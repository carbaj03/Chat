package com.acv.chat.data.openai.chat

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ToolId(val id: String)