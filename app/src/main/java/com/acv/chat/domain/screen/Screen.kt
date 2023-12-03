package com.acv.chat.domain.screen

import arrow.optics.optics

@optics sealed interface Screen {
  val create: () -> Unit
  val update: () -> Unit
  val destroy: () -> Unit

  companion object
}

