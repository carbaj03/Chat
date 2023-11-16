package com.acv.chat.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import arrow.optics.optics
import com.acv.chat.components.Color
import com.acv.chat.components.Icon
import com.acv.chat.components.TextStyle
import com.acv.chat.components.invoke

@optics
data class TimeInput(
  val hour: Int,
  val minute: Int,
  val second: Int,
  val onChange: (Int, Int, Int) -> Unit,
) {
  companion object
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
operator fun TimeInput.invoke(modifier: Modifier = Modifier) {
  var text by remember { mutableStateOf("${if (hour <= 9) "0$hour" else hour}${if (minute <= 9) "0$minute" else minute}${if (second <= 9) "0$second" else second}") }
  var showDialog by remember { mutableStateOf(false) }
  var error by remember { mutableStateOf(false) }

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    BasicTextField(
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
      value = text,
      maxLines = 1,
      onValueChange = {
        if (it.length > 6) return@BasicTextField

        if (it.length == 6) {
          error = false
          onChange(it.substring(0..1).toInt(), it.substring(2..3).toInt(), it.substring(4..5).toInt())
        } else {
          error = true
        }
        text = it
      },
      textStyle = TextStyle.BodyLarge(),
      visualTransformation = TimeTransformation(),
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
      )
    )
    IconButton(onClick = { showDialog = true }) {
      Icon.Time()
    }
  }

  if (showDialog) {
    Dialog(onDismissRequest = { showDialog = false }) {
      Column(
        modifier
          .background(Color.White(), AlertDialogDefaults.shape)
          .padding(16.dp)
      ) {
        val state = rememberTimePickerState(initialHour = hour, initialMinute = minute)
        TimePicker(state = state)

        Text(
          text = "Ok", modifier = Modifier
            .clickable {
              text = "${if (state.hour <= 9) "0${state.hour}" else state.hour}${if (state.minute <= 9) "0${state.minute}" else state.minute}${if (second <= 9) "0$second" else second}"
              onChange(state.hour, state.minute, second)
              showDialog = false
            }
            .align(Alignment.End)
            .padding(16.dp)
        )
      }
    }
  }
}

class TimeTransformation() : VisualTransformation {
  override fun filter(text: AnnotatedString): TransformedText {
    return timeFilter(text)
  }
}

fun timeFilter(text: AnnotatedString): TransformedText {
  val trimmed = if (text.text.length >= 6) text.text.substring(0..5) else text.text
  var out = ""
  for (i in trimmed.indices) {
    out += trimmed[i]
    if (i % 2 == 1 && i < 4) out += ":"
  }

  val numberOffsetTranslator = object : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
      if (offset <= 1) return offset
      if (offset <= 3) return offset + 1
      if (offset <= 5) return offset + 2
      return 8
    }

    override fun transformedToOriginal(offset: Int): Int {
      if (offset <= 2) return offset
      if (offset <= 5) return offset - 1
      if (offset <= 8) return offset - 2
      return 6
    }
  }

  return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}