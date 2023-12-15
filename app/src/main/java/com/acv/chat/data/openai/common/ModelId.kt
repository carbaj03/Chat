package com.acv.chat.data.openai.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ModelIdSerializer::class)
sealed class ModelId(val id: String) {
  data object Gpt4 : ModelId("gpt-4")
  data object Gpt4Preview : ModelId("gpt-4-1106-preview")
  data object Gpt4Vision : ModelId("gpt-4-vision-preview")
  data object Gpt35 : ModelId("gpt-3.5-turbo")
  data object TextEmbeddingAda002 : ModelId("text-embedding-ada-002")
  data object Whisper1 : ModelId("whisper-1")
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = ModelId::class)
object ModelIdSerializer : KSerializer<ModelId> {
  override fun serialize(encoder: Encoder, value: ModelId) {
    encoder.encodeString(value.id)
  }

  override fun deserialize(decoder: Decoder): ModelId {
    val id = decoder.decodeString()
    return when (id) {
      "gpt-4" -> ModelId.Gpt4
      "gpt-4-1106-preview" -> ModelId.Gpt4Preview
      "gpt-4-vision-preview" -> ModelId.Gpt4Vision
      "gpt-3.5-turbo" -> ModelId.Gpt35
      "text-embedding-ada-002" -> ModelId.TextEmbeddingAda002
      "whisper-1" -> ModelId.Whisper1
      else -> throw IllegalArgumentException("Unknown ModelId value: $id")
    }
  }
}