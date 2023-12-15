package com.acv.chat.data.openai.common

import kotlinx.serialization.Serializable

@Serializable(with = FunctionModeSerializer::class)
sealed interface FunctionMode {
  @JvmInline
  value class Default(val value: String) : FunctionMode

  @Serializable
  data class Named(val name: String) : FunctionMode
  companion object {
    val Auto: FunctionMode = Default("auto")
    val None: FunctionMode = Default("none")
  }
}
