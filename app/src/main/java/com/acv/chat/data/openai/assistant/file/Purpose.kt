package com.acv.chat.data.openai.assistant.file

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Purpose(val raw: String) {
  companion object {
    val fineTune = Purpose("fine-tune")
    val fineTuneResults = Purpose("fine-tune-results")
    val assistants = Purpose("assistants")
    val assistantsOutput = Purpose("assistants_output")
  }
}