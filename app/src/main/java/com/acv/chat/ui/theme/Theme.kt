package com.acv.chat.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.acv.chat.components.Theme
import com.acv.chat.components.invoke

@Composable
operator fun Theme.invoke(
  darkTheme: Boolean = isSystemInDarkTheme(),
//  dynamicColor: Boolean = true,
  content: @Composable () -> Unit
) {
//  val colorScheme = when {
//    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//      val context = LocalContext.current
//      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//    }
//    darkTheme -> Dark()
//    else -> Light()
//  }
  val view = LocalView.current
  if (!view.isInEditMode) {
    val color = colors.primary().toArgb()
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = color
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
    }
  }

  MaterialTheme(
    colorScheme = colors(),
    typography = typography(),
    content = content
  )
}