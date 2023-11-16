package com.acv.chat.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import arrow.optics.optics

@optics data class Scaffold(
  val topBar: TopBar,
  val bottomBar: BottomBar,
  val content: () -> Unit
) {
  companion object
}

@Composable
operator fun Scaffold.invoke(
  snackbarHostState: SnackbarHostState,
  modifier: Modifier = Modifier,
  content: @Composable (PaddingValues) -> Unit
) {
  androidx.compose.material3.Scaffold(
    modifier = modifier,
    topBar = { topBar() },
    bottomBar = { bottomBar() },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    floatingActionButton = { },
    content = content
  )
}