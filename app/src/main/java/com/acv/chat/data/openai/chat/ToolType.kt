package com.acv.chat.data.openai.chat

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ToolType(val value: String) {
  companion object {
    val Function: ToolType = ToolType("function")
  }
}
