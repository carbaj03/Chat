package com.acv.chat.data.openai.audio;

import com.acv.chat.data.FileSource
import com.acv.chat.data.openai.common.ModelId;
import com.acv.chat.data.openai.common.ModelId.Whisper1

class TranslationRequest(
  val audio: FileSource,
  val model: ModelId = Whisper1,
  val prompt: String? = null,
  val responseFormat: String? = null,
  val temperature: Double? = null
)
