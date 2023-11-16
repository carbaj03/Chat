package com.acv.chat.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import arrow.optics.optics

@optics
sealed interface TextStyle {
  data object H1 : TextStyle
  data object H2 : TextStyle
  data object H3 : TextStyle
  data object Title : TextStyle
  data object TitleLarge : TextStyle
  data object TitleSmall : TextStyle
  data object Label : TextStyle
  data object Display : TextStyle
  data object Body : TextStyle
  data object BodyLarge : TextStyle
  data object BodySmall : TextStyle

  val Default get() = Body

  companion object
}

@Composable
operator fun TextStyle.invoke(): androidx.compose.ui.text.TextStyle =
  when (this) {
    TextStyle.Body -> style
    TextStyle.BodyLarge -> style
    TextStyle.H1 -> style
    TextStyle.H2 -> style
    TextStyle.H3 -> style
    TextStyle.Title -> style
    TextStyle.TitleLarge -> style
    TextStyle.TitleSmall -> style
    TextStyle.Label -> style
    TextStyle.Display -> style
    TextStyle.BodySmall -> style
  }

internal val TextStyle.style
  get() = DefaultTextStyle.copy(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.2.sp,
  )

internal val DefaultTextStyle =
  androidx.compose.ui.text.TextStyle.Default.copy(platformStyle = PlatformTextStyle(includeFontPadding = true))