package com.acv.chat.components

import arrow.optics.optics

@optics sealed interface Theme {
  val surface: Surface
  val colors: Colors
  val typography: Typography

  @optics data class Dark(
    override val surface: Surface = Surface(),
    override val colors: Colors = Colors(),
    override val typography: Typography = Typography()
  ) : Theme {
    companion object
  }

  @optics data class Light(
    override val surface: Surface = Surface(),
    override val colors: Colors = Colors(),
    override val typography: Typography = Typography()
  ) : Theme {
    companion object
  }

  companion object
}


