package com.acv.chat.domain.screen

import arrow.optics.optics

@optics sealed interface Screen {
  val destroy: () -> Unit

  companion object
}