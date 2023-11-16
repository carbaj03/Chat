package com.acv.chat.components

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import arrow.optics.optics

@optics data class Colors(
  val primary: Color = Color.Black,
  val primaryContainer: Color = Color.Black,
  val inversePrimary: Color = Color.Black,
  val secondary: Color = Color.Teal,
  val secondaryContainer: Color = Color.Black,
  val tertiary: Color = Color.BlueNavy,
  val tertiaryContainer: Color = Color.Black,
  val background: Color = Color.Pastel,
  val onBackground: Color = Color.Black,
  val surface: Color = Color.Transparent,
  val surfaceVariant: Color = Color.Black,
  val surfaceTint: Color = Color.Black,
  val inverseSurface: Color = Color.Black,
  val error: Color = Color.Black,
  val errorContainer: Color = Color.Black,
) {
  companion object
}

internal val LocalColorScheme = staticCompositionLocalOf { Colors() }

@Composable
operator fun Colors.invoke(): ColorScheme = lightColorScheme(
  primary = primary(),
  primaryContainer = primaryContainer(),
  inversePrimary = inversePrimary(),
  secondary = secondary(),
  secondaryContainer = secondaryContainer(),
  tertiary = tertiary(),
  tertiaryContainer = tertiaryContainer(),
  background = background(),
  surface = surface(),
  surfaceVariant = surfaceVariant(),
  surfaceTint = surfaceTint(),
  inverseSurface = inverseSurface(),
  error = error(),
  errorContainer = errorContainer(),
)