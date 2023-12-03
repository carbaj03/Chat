package com.acv.chat.data.openai.file;

import com.acv.chat.data.openai.Usage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ListResponseFile(
  @SerialName("data") val data: List<File>,
  @SerialName("usage") val usage: Usage? = null,
  @SerialName("first_id") val firstId: String? = null,
  @SerialName("last_id") val lastId: String? = null,
  @SerialName("has_more") val hasMore: Boolean? = null,
)