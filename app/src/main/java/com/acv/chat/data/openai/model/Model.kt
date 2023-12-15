package com.acv.chat.data.openai.model

import com.acv.chat.data.openai.common.ModelId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Model(
  @SerialName("id") val id: ModelId,
  @SerialName("created") val created: Long,
  @SerialName("owned_by") val ownedBy: String,
  @SerialName("permission") val permission: List<ModelPermission>? = null,
)