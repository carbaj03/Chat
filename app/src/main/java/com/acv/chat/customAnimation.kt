package com.acv.chat

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntSize

fun AnimatedContentTransitionScope<Boolean>.customAnimation(): ContentTransform =
  fadeIn(
    animationSpec = spring()
  ) togetherWith fadeOut(
    animationSpec = spring()
  ) using SizeTransform { initialSize, targetSize ->
    if (targetState) {
      keyframes {
        IntSize(targetSize.width, initialSize.height) at 150
        durationMillis = 300
      }
    } else {
      keyframes {
        IntSize(initialSize.width, targetSize.height) at 150
        durationMillis = 300
      }
    }
  }