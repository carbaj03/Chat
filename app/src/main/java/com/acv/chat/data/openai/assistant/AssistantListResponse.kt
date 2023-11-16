package com.acv.chat.data.openai.assistant

import kotlinx.serialization.Serializable

@Serializable
data class AssistantListResponse(
    val `object`: String,
    val data: List<AssistantObjectBeta>,
    val first_id: String? = null,
    val last_id: String? = null,
    val has_more: Boolean
)