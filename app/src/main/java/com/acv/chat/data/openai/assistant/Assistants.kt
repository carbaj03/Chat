package com.acv.chat.data.openai.assistant

import kotlinx.serialization.Serializable

@Serializable
data class Assistants(
  val data: List<Assistant>,
  val first_id: String? = null,
  val last_id: String? = null,
  val has_more: Boolean
)