package com.acv.chat.data.openai.audio

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class AudioResponseFormat(val value: String) {
  companion object {
    val Json: AudioResponseFormat = AudioResponseFormat("json")
    val Text: AudioResponseFormat = AudioResponseFormat("text")
    val Srt: AudioResponseFormat = AudioResponseFormat("srt")
    val VerboseJson: AudioResponseFormat = AudioResponseFormat("verbose_json")
    val Vtt: AudioResponseFormat = AudioResponseFormat("vtt")
  }
}