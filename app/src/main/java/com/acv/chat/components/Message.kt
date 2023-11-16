package com.acv.chat.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arrow.optics.optics

@optics data class Message(
  val label: Text,
  val text: Text,
) {
  companion object
}

@Composable
operator fun Message.invoke(modifier: Modifier = Modifier) {
  Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
    label()
    text()
  }
}