package com.acv.chat.data.openai.assistant.file

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Purpose(val raw: String) {
  companion object {
    val fineTune = Purpose("fine-tune")
    val assistants = Purpose("assistants")
  }
}