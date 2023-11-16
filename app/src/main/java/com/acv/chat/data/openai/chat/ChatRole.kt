package com.acv.chat.data.openai.chat

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ChatRole(val role: String) {
  companion object {
    val System: ChatRole = ChatRole("system")
    val User: ChatRole = ChatRole("user")
    val Assistant: ChatRole = ChatRole("assistant")
    val Tool: ChatRole = ChatRole("tool")
  }
}
