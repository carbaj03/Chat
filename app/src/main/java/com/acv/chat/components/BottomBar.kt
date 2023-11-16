package com.acv.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomAppBarDefaults.windowInsets
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import arrow.optics.optics
import com.acv.chat.components.input.OutlinedInput

@optics data class BottomBar(
  val input: OutlinedInput,
  val gallery: ButtonIcon,
  val photo: ButtonIcon,
  val audio: ButtonIcon,
  val translation: ButtonIcon,
  val send: ButtonIcon,
) {
  companion object
}

@Composable
operator fun BottomBar.invoke(modifier: Modifier = Modifier) {
  Surface(
    color = Color.White,
    contentColor = Color.Black,
    tonalElevation = 3.0.dp,
    shape = RectangleShape,
    modifier = modifier
  ) {
    Row(
      Modifier
        .fillMaxWidth()
        .windowInsetsPadding(windowInsets)
        .height(60.0.dp)
        .padding(PaddingValues(4.dp, 4.dp)),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically,
      content = {
        gallery()
        photo()
        audio()
        translation()

        Spacer(modifier = Modifier.weight(1f))

        send()
      }
    )
  }
}