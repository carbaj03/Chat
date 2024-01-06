package com.acv.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import arrow.optics.optics

@optics data class Surface(
  val color: Color = Color.Transparent
) {
  companion object
}

@Composable
@NonRestartableComposable
operator fun Surface.invoke(
  modifier: Modifier = Modifier,
  shape: Shape = RectangleShape,
  contentColor: Color = contentColorFor(color),
  tonalElevation: Dp = 0.dp,
  shadowElevation: Dp = 0.dp,
  border: BorderStroke? = null,
  content: @Composable () -> Unit
) {
  val absoluteElevation = LocalAbsoluteTonalElevation.current + tonalElevation
  CompositionLocalProvider(
    LocalContentColor provides contentColor,
    LocalAbsoluteTonalElevation provides absoluteElevation
  ) {
    Box(
      modifier = modifier
        .surface(
          shape = shape,
          backgroundColor = color,
          border = border,
          shadowElevation = shadowElevation
        )
        .semantics(mergeDescendants = false) {
          isTraversalGroup = true
        }
        .pointerInput(Unit) {},
      propagateMinConstraints = true
    ) {
      content()
    }
  }
}

val LocalContentColor = compositionLocalOf { Color.Black }

private fun Modifier.surface(
  shape: Shape,
  backgroundColor: Color,
  border: BorderStroke?,
  shadowElevation: Dp
): Modifier =
  this.shadow(shadowElevation, shape, clip = false)
    .then(if (border != null) Modifier.border(border, shape) else Modifier)
    .background(color = backgroundColor, shape = shape)
    .clip(shape)

val LocalAbsoluteTonalElevation = compositionLocalOf { 0.dp }