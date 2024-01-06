package com.acv.chat.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.acv.chat.components.Gesture.Cancel
import com.acv.chat.components.Gesture.Press
import com.acv.chat.components.Gesture.Released

data class TextSpannedStyle(
  val text: String,
  val textStyle: TextStyle? = null,
  val fontWeight: FontWeight? = null,
  val textDecoration: TextDecoration? = null,
  val color: Color = Color.Unspecified,
  val onClick: (Int, String) -> Unit = { _, _ -> },
) {
  var pressed by mutableStateOf(false)
}

@Composable
fun TextSpanned(
  vararg textSpanned: TextSpannedStyle,
  modifier: Modifier = Modifier,
  text: String,
  style: TextStyle = LocalTextStyle.current,
  info: (List<String>) -> Unit = {},
  color: Color = Color.Unspecified,
  textDecoration: TextDecoration? = null,
  fontWeight: FontWeight? = null,
  textAlign: TextAlign = TextAlign.Unspecified,
  lineHeight: TextUnit = TextUnit.Unspecified,
  overflow: TextOverflow = TextOverflow.Clip,
  softWrap: Boolean = true,
  maxLines: Int = Int.MAX_VALUE,
  onTextLayout: (TextLayoutResult) -> Unit = {},
) {
  if (textSpanned.none { text.contains(it.text) }) {
    Text(
      modifier = modifier,
      text = text,
      style = style,
      textAlign = textAlign,
      color = color,
      lineHeight = lineHeight,
      onTextLayout = onTextLayout,
      maxLines = maxLines,
      overflow = overflow,
      fontWeight = fontWeight,
      textDecoration = textDecoration,
      softWrap = softWrap,
    )
  } else {
    val merge = style.merge(
      TextStyle(
//        brush = style.brush,
        color = color,
        fontSize = style.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        lineHeight = lineHeight,
        fontFamily = style.fontFamily,
        textDecoration = textDecoration,
        fontStyle = style.fontStyle,
        letterSpacing = style.letterSpacing,
      )
    )

    val annotatedString = buildAnnotatedString {
      withStyle(
        style = SpanStyle(
//          brush = merge.brush,
          color = merge.color,
          fontFamily = merge.fontFamily,
          fontSize = merge.fontSize,
          fontWeight = merge.fontWeight,
          textDecoration = merge.textDecoration,
        )
      ) {
        append(text)
      }

      textSpanned.forEach {
        apply(
          text = text,
          textSpannedStyle = it,
          action = { start, end ->
            addStyle(
              style = SpanStyle(
                color = if (it.pressed) it.color.copy(alpha = 0.5f) else it.color,
                fontFamily = it.textStyle?.fontFamily,
                fontSize = it.textStyle?.fontSize ?: TextUnit.Unspecified,
                fontWeight = it.fontWeight,
                textDecoration = it.textDecoration,
                letterSpacing = it.textStyle?.letterSpacing ?: TextUnit.Unspecified,
              ),
              start = start,
              end = end
            )
          }
        )
      }
    }

    LaunchedEffect(Unit) {
      info(
        annotatedString.spanStyles.map {
          "spanStyles: ${it.start} - ${it.end} - ${text.substring(it.start, it.end)}"
        }
      )
    }

    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    BasicText(
      text = annotatedString,
      modifier = modifier
        .pointerInput(Unit) {
          awaitEachGesture {
            val first = awaitFirstDown()
            val position = layoutResult.value?.getOffsetForPosition(first.position)

            val action = { gesture: Gesture ->
              var end = false
              val iterator = textSpanned.reversed().iterator()

              while (iterator.hasNext() && !end) {
                val it = iterator.next()
                var startIndex = 0
                while (startIndex < text.length) {
                  val index = text.indexOf(it.text, startIndex)
                  if (index >= 0) {
                    startIndex = index + it.text.length
                    if (position in index..<startIndex) {
                      when (gesture) {
                        Press -> {
                          it.pressed = true
                        }
                        Released -> {
                          it.onClick(position!!, it.text)
                          it.pressed = false
                        }
                        Cancel -> {
                          it.pressed = false
                        }
                      }
                      end = true
                    }
                  } else {
                    break
                  }
                }
              }
            }

            first.consume()
            action(Press)

            val up = waitForUpOrCancellation()
            if (up != null) {
              up.consume()
              action(Released)
            } else {
              action(Cancel)
            }
          }
        },
      style = style,
      softWrap = softWrap,
      overflow = overflow,
      maxLines = maxLines,
      onTextLayout = {
        layoutResult.value = it
        onTextLayout(it)
      }
    )
  }
}

enum class Gesture {
  Press, Released, Cancel
}

private inline fun apply(
  text: String,
  textSpannedStyle: TextSpannedStyle,
  action: (Int, Int) -> Unit
) {
  var startIndex = 0
  while (startIndex < text.length) {
    val index = text.indexOf(textSpannedStyle.text, startIndex)
    if (index >= 0) {
      startIndex = index + textSpannedStyle.text.length
      action(index, startIndex)
    } else {
      break
    }
  }
}