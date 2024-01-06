package com.acv.chat

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed interface SpeachState {
  data object Normal : SpeachState
  data object Loading : SpeachState
  data object Error : SpeachState
}

@Composable
fun Speech(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var state: SpeachState by remember { mutableStateOf(SpeachState.Normal) }
  val scope = rememberCoroutineScope()

  when (state) {
    SpeachState.Error -> {
      SpeechError(
        onSpeechClick = onClick,
        onTryAgainClick = { state = SpeachState.Normal },
        modifier = modifier
      )
    }
    SpeachState.Loading -> {
      SpeechLoading(
        modifier = modifier
      )
    }
    SpeachState.Normal -> {
      SpeechContent(
        onClick = {
          scope.launch {
            state = SpeachState.Loading
            delay(1000)
            state = SpeachState.Error
          }
        },
        modifier = modifier
      )
    }
  }
}

@Composable
fun SpeechContent(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var counter by remember { mutableIntStateOf(0) }
  val infiniteTransition = rememberInfiniteTransition(label = "")
  val size by infiniteTransition.animateValue(
    initialValue = 40.dp,
    targetValue = 70.dp,
    typeConverter = Dp.VectorConverter,
    animationSpec = infiniteRepeatable(
      animation = tween(500, easing = FastOutLinearInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = ""
  )

  LaunchedEffect(Unit) {
    while (true) {
      delay(1000)
      counter += 1
    }
  }

  Box(
    modifier = modifier
      .clickable { onClick() }
  ) {
    Text(
      text = counter.toHoursMinuteSeconds(),
      modifier = Modifier.padding(16.dp),
      color = MaterialTheme.colorScheme.onTertiaryContainer
    )

    Box(
      modifier = Modifier
        .size(size)
        .align(Alignment.Center)
        .background(MaterialTheme.colorScheme.tertiary, shape = CircleShape)
    )

    Text(
      text = "Talk",
      modifier = Modifier.align(Alignment.Center),
    )
  }
}

@Composable
fun SpeechLoading(
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.align(Alignment.Center),
      verticalAlignment = Alignment.CenterVertically
    ) {
      CircularProgressIndicator(color = MaterialTheme.colorScheme.onTertiaryContainer)
      Spacer(modifier = Modifier.width(8.dp))
      Text(text = "Converting to text...", color = MaterialTheme.colorScheme.onTertiaryContainer)
    }
  }
}

@Composable
fun SpeechError(
  onSpeechClick: () -> Unit,
  onTryAgainClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
  ) {
    IconButton(
      onClick = onSpeechClick,
      modifier = Modifier.align(Alignment.TopEnd),
    ) {
      Icon(
        imageVector = Icons.Default.Close,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onTertiaryContainer
      )
    }

    Column(
      modifier = Modifier.align(Alignment.Center)
    ) {
      Text(
        text = "No speech detected",
        modifier = Modifier.align(Alignment.CenterHorizontally),
        color = MaterialTheme.colorScheme.onTertiaryContainer
      )
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        onClick = onTryAgainClick,
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.secondaryContainer,
        )
      ) {
        Text(text = "Start new recording", color = MaterialTheme.colorScheme.onSecondaryContainer)
      }
    }
  }
}