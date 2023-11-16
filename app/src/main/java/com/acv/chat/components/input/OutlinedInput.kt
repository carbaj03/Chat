package com.acv.chat.components.input

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import arrow.optics.optics
import com.acv.chat.components.Text
import com.acv.chat.components.invoke

@optics
data class OutlinedInput(
  val text: Text,
  val onChange: (String) -> Unit,
  val placeholder: Text? = null,
) {
  companion object
}

@Composable
operator fun OutlinedInput.invoke(modifier: Modifier = Modifier) {
  OutlinedTextField(
    modifier = modifier,
    value = text.value,
    onValueChange = onChange,
    textStyle = text.style(),
    placeholder = placeholder?.let { { this(modifier) } }
  )
}
