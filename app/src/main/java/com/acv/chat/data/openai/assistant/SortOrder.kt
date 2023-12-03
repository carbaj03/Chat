package com.acv.chat.data.openai.assistant

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class SortOrder(val order: String) {
  companion object {
    val Ascending: SortOrder = SortOrder("asc")
    val Descending: SortOrder = SortOrder("desc")
  }
}
