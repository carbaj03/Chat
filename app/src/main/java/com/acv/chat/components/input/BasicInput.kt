package com.acv.chat.components.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import arrow.optics.optics
import com.acv.chat.components.Text
import com.acv.chat.components.invoke

@optics
data class BasicInput(
  val text: Text,
  val onChange: (String) -> Unit,
  val enabled: Boolean = true,
  val placeholder: Text? = null,
) {
  companion object
}

@Composable
operator fun BasicInput.invoke(modifier: Modifier = Modifier) {
  var field by remember(text.value) { mutableStateOf(TextFieldValue(text.value, TextRange(text.value.length))) }

  Box(modifier = modifier) {
    if (text.value.isEmpty()) placeholder?.let { it() }

    BasicTextField(
      modifier = Modifier.fillMaxWidth(),
      value = field,
      onValueChange = { onChange(it.text); field = it },
      textStyle = text.style(),
      enabled = enabled,
//      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//      keyboardActions = KeyboardActions(
//        onDone = {  }
//      )
    )
  }
}