package com.acv.chat.data.openai.assistant.message

import com.acv.chat.data.openai.common.Usage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Messages(
  @SerialName("data") val data: List<Message>,
  @SerialName("usage") val usage: Usage? = null,
  @SerialName("first_id") val firstId: String? = null,
  @SerialName("last_id") val lastId: String? = null,
  @SerialName("has_more") val hasMore: Boolean? = null,
)

val Messages.firstMessage: String?
  get() = data.first().content.first().text()?.text?.value