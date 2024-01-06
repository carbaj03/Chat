package com.acv.chat.components

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import arrow.optics.optics

@optics data class Colors(
  val primary: Color,
  val primaryContainer: Color,
  val onPrimary: Color,
  val inversePrimary: Color,
  val secondary: Color,
  val secondaryContainer: Color,
  val onSecondary: Color,
  val tertiary: Color,
  val tertiaryContainer: Color,
  val onTertiaryContainer: Color,
  val onTertiary: Color,
  val background: Color,
  val onBackground: Color,
  val surface: Color,
  val onSurface: Color,
  val surfaceVariant: Color,
  val onSurfaceVariant: Color,
  val surfaceTint: Color,
  val inverseSurface: Color,
  val error: Color,
  val errorContainer: Color,
) {
  companion object
}

val lightColors = Colors(
  primary = Color.Black,
  onPrimary = Color.White,
  primaryContainer = Color.White,
  inversePrimary = Color.Black,
  secondary = Color.Teal,
  onSecondary = Color.White,
  secondaryContainer = Color.Black,
  tertiary = Color.BlueNavy,
  onTertiary = Color.White,
  tertiaryContainer = Color.Purple,
  onTertiaryContainer = Color.White,
  background = Color.White,
  onBackground = Color.Black,
  surface = Color.Transparent,
  onSurface = Color.Black,
  surfaceVariant = Color.GrayPastel,
  onSurfaceVariant = Color.Black,
  surfaceTint = Color.Black,
  inverseSurface = Color.Black,
  error = Color.Red,
  errorContainer = Color.Black,
)

val darkColors = Colors(
  primary = Color.DarkGray,
  onPrimary = Color.White,
  primaryContainer = Color.DarkGray,
  inversePrimary = Color.White,
  secondary = Color.GrayPastel,
  onSecondary = Color.Purple,
  secondaryContainer = Color.White,
  tertiary = Color.White,
  onTertiary = Color.Purple,
  tertiaryContainer = Color.Purple,
  onTertiaryContainer = Color.White,
  background = Color.DarkGray,
  onBackground = Color.White,
  surface = Color.DarkGray,
  onSurface = Color.White,
  surfaceVariant = Color.GrayMid,
  onSurfaceVariant = Color.White,
  surfaceTint = Color.Black,
  inverseSurface = Color.Black,
  error = Color.Red,
  errorContainer = Color.Black,
)

internal val LocalColorScheme = staticCompositionLocalOf { lightColors }

@Composable
operator fun Colors.invoke(): ColorScheme =
  ColorScheme(
    primary = primary(),
    onPrimary = onPrimary(),
    primaryContainer = primaryContainer(),
    onPrimaryContainer = onPrimary(),
    inversePrimary = inversePrimary(),
    secondary = secondary(),
    onSecondary = onSecondary(),
    secondaryContainer = secondaryContainer(),
    onSecondaryContainer = onSecondary(),
    tertiary = tertiary(),
    onTertiary = onTertiary(),
    tertiaryContainer = tertiaryContainer(),
    onTertiaryContainer = onTertiaryContainer(),
    background = background(),
    onBackground = onBackground(),
    surface = surface(),
    onSurface = onSurface(),
    surfaceVariant = surfaceVariant(),
    onSurfaceVariant = onSurfaceVariant(),
    surfaceTint = surfaceTint(),
    inverseSurface = inverseSurface(),
    inverseOnSurface = inverseSurface(),
    error = error(),
    onError = error(),
    errorContainer = errorContainer(),
    onErrorContainer = errorContainer(),
    outline = Color.Transparent(),
    outlineVariant = Color.Transparent(),
    scrim = Color.Transparent(),
  )