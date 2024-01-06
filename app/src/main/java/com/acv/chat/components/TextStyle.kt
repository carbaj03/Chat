package com.acv.chat.components

import androidx.compose.material3.MaterialTheme
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
    TextStyle.Body -> MaterialTheme.typography.bodyMedium
    TextStyle.BodyLarge -> MaterialTheme.typography.bodyLarge
    TextStyle.BodySmall -> MaterialTheme.typography.bodySmall
    TextStyle.H1 -> MaterialTheme.typography.headlineSmall
    TextStyle.H2 -> MaterialTheme.typography.headlineMedium
    TextStyle.H3 -> MaterialTheme.typography.headlineLarge
    TextStyle.Title -> MaterialTheme.typography.titleMedium
    TextStyle.TitleLarge -> MaterialTheme.typography.titleLarge
    TextStyle.TitleSmall -> MaterialTheme.typography.titleSmall
    TextStyle.Label -> MaterialTheme.typography.labelMedium
    TextStyle.Display -> MaterialTheme.typography.displayMedium
  }

internal val DefaultTextStyle =
  androidx.compose.ui.text.TextStyle.Default.copy(platformStyle = PlatformTextStyle(includeFontPadding = true))