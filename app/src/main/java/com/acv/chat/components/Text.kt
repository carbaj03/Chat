package com.acv.chat.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import arrow.optics.optics

@optics
data class Text(
  val value: String,
  val style: TextStyle = TextStyle.Body,
  val color: Color = Color.Black,
) {
  companion object
}

@Composable
operator fun Text.invoke(modifier: Modifier = Modifier) {
  Text(
    modifier = modifier,
    text = value,
    color = color(),
    style = style()
  )
}