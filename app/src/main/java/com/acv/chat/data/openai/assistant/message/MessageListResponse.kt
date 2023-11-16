package com.acv.chat.data.openai.assistant.message

import kotlinx.serialization.Serializable

@Serializable
data class MessageListResponse(
    val `object`: String,
    val data: List<MessageResponse>,
    val first_id: String,
    val last_id: String,
    val has_more: Boolean
)