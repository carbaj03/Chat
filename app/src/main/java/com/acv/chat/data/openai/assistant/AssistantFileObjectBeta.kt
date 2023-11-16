package com.acv.chat.data.openai.assistant

import kotlinx.serialization.Serializable

@Serializable
data class AssistantFileObjectBeta(
    val id: String,
    val `object`: String = "assistant.file",
    val created_at: Int,
    val assistant_id: String
)