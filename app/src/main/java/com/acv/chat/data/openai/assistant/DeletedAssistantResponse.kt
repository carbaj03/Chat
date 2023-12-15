package com.acv.chat.data.openai.assistant

import kotlinx.serialization.Serializable

@Serializable
data class DeletedAssistantResponse(
  val id: String,
  val deleted: Boolean
)