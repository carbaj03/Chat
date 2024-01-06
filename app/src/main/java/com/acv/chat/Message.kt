package com.acv.chat

import com.acv.chat.domain.Media
import kotlinx.serialization.Serializable

@Serializable
sealed interface Message {
  val text: String

  @Serializable
  data class Human(
    override val text: String,
    val files: List<Media> = emptyList()
  ) : Message

  @Serializable
  data class Assistant(
    override val text: String
  ) : Message
}
