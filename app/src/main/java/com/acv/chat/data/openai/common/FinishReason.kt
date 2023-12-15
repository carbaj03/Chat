package com.acv.chat.data.openai.common

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class FinishReason(val value: String) {
  companion object {
    val Stop: FinishReason = FinishReason("stop")
    val Length: FinishReason = FinishReason("length")
    val FunctionCall: FinishReason = FinishReason("function_call")
  }
}
