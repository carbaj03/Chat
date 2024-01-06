package com.acv.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.acv.chat.Model.GPT4
import com.acv.chat.domain.Media
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Composable
fun Content(
  onMenuClick: () -> Unit,
) {
  val scope = rememberCoroutineScope()
  val lazyListState = rememberLazyListState()

  var items: List<Message> by rememberSaveable(stateSaver = MessagesSaver) { mutableStateOf(emptyList()) }
  var model: Model by rememberSaveable { mutableStateOf(GPT4) }
  var showDialog by remember { mutableStateOf(false) }
  var showFile: Media? by remember { mutableStateOf(null) }

  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    var speech by remember { mutableStateOf(false) }
    Scaffold(
      modifier = Modifier.weight(1f),
      topBar = {
        TopBarChat(
          title = model.value,
          onViewDetailsClick = { showDialog = true },
          onNewChatClick = { items = emptyList() },
          onMenuClick = onMenuClick,
          onChangeModelClick = { model = it },
          model = model,
          isNew = items.isEmpty()
        )
      },
      bottomBar = {
        BottomChat(
          isMultimodal = model == GPT4,
          onSend = { msg, files ->
            items = items + Message.Human(msg, files)
          },
          onAssistant = {
            scope.launch {
              items = items + Message.Assistant("")
              "Hello world from the other side of the world".forEach {
                delay(50)
                items = items.toMutableList().apply { set(items.size - 1, Message.Assistant(items.last().text + it)) }
              }
            }
          },
          onSpeech = { speech = !speech },
          speech = speech,
          fileClick = { showFile = it },
          modifier = if (!speech) Modifier.navigationBarsPadding() else Modifier
        )
      }
    ) {
      LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.totalItemsCount }.collect { lazyListState.scrollToItem(it) }
      }

      val isBottom by remember(lazyListState) { derivedStateOf { lazyListState.canScrollForward } }

      Box(
        modifier = Modifier.padding(it).fillMaxSize()
      ) {
        LazyColumn(state = lazyListState) {
          items(items) {
            ChatRow(
              msg = it,
              fileClick = { showFile = it }
            )
          }
        }

        if (items.isEmpty()) {
          Image(
            modifier = Modifier.size(52.dp).clip(CircleShape).align(Alignment.Center),
            painter = painterResource(R.drawable.chatgpt_logo),
            contentDescription = null
          )
        }

        if (isBottom)
          SmallFloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
            shape = CircleShape,
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(bottom = 8.dp),
            onClick = { scope.launch { lazyListState.scrollToItem(items.size - 1) } }
          ) {
            Icon(
              imageVector = Icons.Default.ArrowDropDown,
              contentDescription = null
            )
          }
      }
    }

    AnimatedVisibility(speech) {
      Speech(
        modifier = Modifier
          .background(MaterialTheme.colorScheme.tertiaryContainer)
          .height(400.dp)
          .windowInsetsPadding(BottomAppBarDefaults.windowInsets)
          .fillMaxWidth(),
        onClick = { speech = false },
      )
    }
  }

  showFile?.let {
    FullScreenDialog(
      onClose = { showFile = null },
      file = it
    )
  }

  if (showDialog) {
    FullScreenDialog(
      onClose = { showDialog = false }
    )
  }
}

fun Int.toHoursMinuteSeconds(): String {
  val hours = this / 3600f
  val fullHours = hours.toInt()

  val minutes = (hours - fullHours) * 60f
  val fullMinutes = minutes.toInt()

  val seconds = (minutes - fullMinutes) * 60f
  val fullSeconds = seconds.toInt()

  return "${if (fullMinutes < 10) "0$fullMinutes" else fullMinutes}:${if (fullSeconds < 10) "0$fullSeconds" else fullSeconds}"
}


@Composable
fun rememberInteractionSource(
  onPress: () -> Unit
): MutableInteractionSource = remember {
  object : MutableInteractionSource {
    override val interactions = MutableSharedFlow<Interaction>(
      extraBufferCapacity = 16,
      onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override suspend fun emit(interaction: Interaction) {
      if (interaction is PressInteraction.Press) {
        onPress()
      }

      interactions.emit(interaction)
    }

    override fun tryEmit(interaction: Interaction): Boolean {
      return interactions.tryEmit(interaction)
    }
  }
}

@Preview
@Composable
fun ChatPreview() {
  Chat({}, {})
}