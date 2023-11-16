package com.acv.chat.data.openai

import kotlinx.serialization.Serializable

@Serializable
enum class ModelId(val id: String) {
  Gpt4("gpt-4-1106-preview"),
  Gpt4Vision("gpt-4-vision-preview"),
  Gpt35("gpt-3.5-turbo"),
  TextEmbeddingAda002("text-embedding-ada-002"),
  Whisper1("whisper-1")
}