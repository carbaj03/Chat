package com.acv.chat.data.openai.file;

import kotlinx.serialization.Serializable;

@Serializable
data class DeletionStatus(
  val id: String,
  val deleted: Boolean
)