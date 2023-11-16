package com.acv.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arrow.optics.optics

@optics data class TopBar(
  val title: Text,
  val navigationIcon: ButtonIcon? = null,
) {
  companion object
}

@Composable
operator fun TopBar.invoke() {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(color = Color.White())
      .height(56.dp)
      .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    title()
    navigationIcon?.let { icon ->
      icon()
    }
  }
}