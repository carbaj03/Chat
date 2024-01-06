package com.acv.chat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Chat(
  onMenuClick: () -> Unit,
  onSettingsClick: () -> Unit
) {
  val drawerState = rememberDrawerState(DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  DismissibleNavigationDrawer(
    modifier = Modifier.imePadding(),
    drawerState = drawerState,
    drawerContent = {
      DismissibleDrawerSheet(
        modifier = Modifier.width(360.dp),
      ) {
        SearchDrawer(
          onMenuClick = onMenuClick,
          onSettingsClick = onSettingsClick,
        )
      }
    }
  ) {
    Content(
      onMenuClick = {
        scope.launch { drawerState.open() }
      },
    )

    val minValue = -with(LocalDensity.current) { 360.0.dp.toPx() }
    val maxValue = 0f

    Canvas(
      modifier = Modifier.fillMaxSize()
    ) {
      drawRect(
        color = Color.Black.copy(alpha = 0.5f),
        alpha = calculateFraction(minValue, maxValue, drawerState.offset.value)
      )
    }
  }
}

private fun calculateFraction(a: Float, b: Float, pos: Float) =
  ((pos - a) / (b - a)).coerceIn(0f, 1f)