package com.acv.chat.domain.screen

import arrow.optics.optics

@optics sealed interface Screen {
  val init: () -> Unit
  val update: () -> Unit
  val stop: () -> Unit

  companion object
}

