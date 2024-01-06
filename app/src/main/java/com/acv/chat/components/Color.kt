package com.acv.chat.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import arrow.optics.optics
import com.acv.chat.ui.theme.BlueNavy
import com.acv.chat.ui.theme.BluePastel
import com.acv.chat.ui.theme.GreenPastel
import com.acv.chat.ui.theme.Pastel
import com.acv.chat.ui.theme.Teal
import com.acv.chat.ui.theme.YellowPastel
import androidx.compose.ui.graphics.Color as ComposeColor

@optics
sealed interface Color {
  data object White : Color
  data object Red : Color
  data object Green : Color
  data object Blue : Color
  data object BlueNavy : Color
  data object Black : Color
  data object DarkGray : Color
  data object GrayPastel : Color
  data object GrayMid : Color
  data object Purple : Color
  data object Pastel : Color
  data object Teal : Color
  data object Yellow : Color
  data object Transparent : Color
  data object None : Color

  companion object
}

@Composable
operator fun Color.invoke(): androidx.compose.ui.graphics.Color =
  when (this) {
    Color.BlueNavy -> ComposeColor.BlueNavy
    Color.Blue -> ComposeColor.BluePastel
    Color.Green -> ComposeColor.GreenPastel
    Color.Black -> ComposeColor.Black
    Color.Red -> ComposeColor.Red
    Color.Purple -> ComposeColor(0xFF4A4458)
    Color.White -> ComposeColor.White
    Color.Pastel -> ComposeColor.Pastel
    Color.Teal -> ComposeColor.Teal
    Color.Yellow -> ComposeColor.YellowPastel
    Color.Transparent -> ComposeColor.Transparent
    Color.None -> ComposeColor.Unspecified
    Color.DarkGray ->ComposeColor(0xFF1B1A1D)
    Color.GrayPastel -> ComposeColor.GrayPastel
    Color.GrayMid -> ComposeColor(0xFF333335)
  }

val ComposeColor.Companion.GrayPastel get() = ComposeColor(0xFFe3e1e2)


val outlineColor: TextFieldColors @Composable get() =
  OutlinedTextFieldDefaults.colors(
    disabledBorderColor = androidx.compose.ui.graphics.Color.Transparent,
    errorBorderColor = androidx.compose.ui.graphics.Color.Transparent,
    focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
    unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
  )