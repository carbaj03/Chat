package com.acv.chat.data.openai.audio;

import com.acv.chat.data.FileSource
import com.acv.chat.data.openai.common.ModelId;
import com.acv.chat.data.openai.common.ModelId.Whisper1

data class TranscriptionRequest(
  val audio: FileSource,
  val model: ModelId = Whisper1,
  val prompt: String? = null,
  val responseFormat: AudioResponseFormat? = null,
  val temperature: Double? = null,
  val language: Language? = null,
)

enum class Language(val iso6391: String) {
  English("en"),
  Chinese("zh"),
  French("fr"),
  German("de"),
  Italian("it"),
  Japanese("ja"),
  Korean("ko"),
  Portuguese("pt"),
  Spanish("es"),
  Russian("ru"),
  Arabic("ar"),
  Dutch("nl"),
  Polish("pl"),
  Swedish("sv"),
  Turkish("tr"),
  Hindi("hi"),
}