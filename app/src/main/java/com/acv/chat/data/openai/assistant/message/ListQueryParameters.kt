package com.acv.chat.data.openai.assistant.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListQueryParameters(
  @SerialName("limit") val limit: Int? = 20,
  @SerialName("order") val order: String? = "desc",
  @SerialName("after") val after: String? = null,
  @SerialName("before") val before: String? = null
)