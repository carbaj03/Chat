package com.acv.chat.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object AppTheme {
  val colors: Colors
    @Composable
    @ReadOnlyComposable
    get() = LocalColorScheme.current
}

