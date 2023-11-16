package com.acv.chat.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import arrow.optics.optics

@optics data class ButtonIcon(
  val icon: Icon,
  val onClick: () -> Unit,
) {
  companion object
}

@Composable
operator fun ButtonIcon.invoke(): Unit =
  IconButton(onClick = onClick) {
    icon()
  }