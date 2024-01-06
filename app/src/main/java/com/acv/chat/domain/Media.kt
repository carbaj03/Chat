package com.acv.chat.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface Media {
  val file: String

  @Serializable
  data class Pdf(override val file: String) : Media

  @Serializable
  data class Image(override val file: String) : Media
}