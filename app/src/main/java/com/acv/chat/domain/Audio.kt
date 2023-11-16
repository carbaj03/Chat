package com.acv.chat.domain

@JvmInline value class Audio(
  val path: String,
)

data class Transcription(
  val text: String,
)

data class Translation(
  val text: String,
)