package com.acv.chat.data.openai.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ModelId(val id: String)