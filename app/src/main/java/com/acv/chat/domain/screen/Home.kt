package com.acv.chat.domain.screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import arrow.core.raise.effect
import arrow.core.raise.fold
import arrow.core.toNonEmptyListOrNull
import arrow.optics.optics
import coil.compose.rememberAsyncImagePainter
import com.acv.chat.arrow.error.action
import com.acv.chat.arrow.optics.get
import com.acv.chat.arrow.optics.invoke
import com.acv.chat.components.BottomBar
import com.acv.chat.components.ButtonIcon
import com.acv.chat.components.Color
import com.acv.chat.components.Icon
import com.acv.chat.components.Message
import com.acv.chat.components.Text
import com.acv.chat.components.TextStyle.Label
import com.acv.chat.components.TopBar
import com.acv.chat.components.audio
import com.acv.chat.components.icon
import com.acv.chat.components.input
import com.acv.chat.components.input.BasicInput
import com.acv.chat.components.input.OutlinedInput
import com.acv.chat.components.input.enabled
import com.acv.chat.components.input.invoke
import com.acv.chat.components.input.text
import com.acv.chat.components.invoke
import com.acv.chat.components.send
import com.acv.chat.components.value
import com.acv.chat.data.openai.audio.AudioService
import com.acv.chat.data.openai.chat.ChatService
import com.acv.chat.domain.App
import com.acv.chat.domain.AppOptics
import com.acv.chat.domain.AudioPlayer
import com.acv.chat.domain.AudioRecorder
import com.acv.chat.domain.MediaService
import com.acv.chat.domain.PhotoService
import com.acv.chat.domain.Store
import com.acv.chat.domain.screen
import com.acv.chat.util.toUri
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

enum class State {
  Searching, Thinking, Idle
}

@optics data class Home(
  val topBar: TopBar,
  val bottomBar: BottomBar,
  val messages: List<Message>,
  val files: List<File> = emptyList(),
  val input: BasicInput,
  val error: String? = null,
  val state: State,
  val scrollState: ScrollState,
  override val init: () -> Unit,
  override val update: () -> Unit,
  override val stop: () -> Unit
) : Screen {
  companion object
}

@optics data class ScrollState(
  val position: Int,
  val offset: Int,
) {
  companion object
}

context(Store<App>, PhotoService, MediaService, AudioPlayer, AudioRecorder, ChatService, AudioService)
fun AppOptics.createHome(): Home = screen.home {
  val load: suspend () -> Unit = {
    delay(1000)
    messages set listOf(
      Message(
        label = Text(value = "Assistant", style = Label, color = Color.BlueNavy),
        text = Text(value = "Hola My Friend")
      ),

      Message(
        label = Text(value = "Assistant", style = Label, color = Color.BlueNavy),
        text = Text(value = "Hola My Friend")
      ),
      Message(
        label = Text(value = "Me", style = Label, color = Color.Blue),
        text = Text(value = "Hola My Friend")
      ),
      Message(
        label = Text(value = "Assistant", style = Label, color = Color.BlueNavy),
        text = Text(value = "Hola My Friend")
      ),
      Message(
        label = Text(value = "Me", style = Label, color = Color.Blue),
        text = Text(value = "Hola My Friend")
      ),
      Message(
        label = Text(value = "Assistant", style = Label, color = Color.BlueNavy),
        text = Text(value = "Hola My Friend")
      ),
      Message(
        label = Text(value = "Me", style = Label, color = Color.Blue),
        text = Text(value = "Hola My Friend")
      ),
      Message(
        label = Text(value = "Assistant", style = Label, color = Color.BlueNavy),
        text = Text(value = "Hola My Friend")
      ),
      Message(
        label = Text(value = "Me", style = Label, color = Color.Blue),
        text = Text(value = "Hola My Friend")
      ),
    )
  }

  Home(
    topBar = TopBar(title = Text("Home")),
    bottomBar = BottomBar(
      input = OutlinedInput(
        text = Text(""),
        onChange = { bottomBar.input.text.value set it },
      ),
      gallery = ButtonIcon(
        icon = Icon.Gallery,
        onClick = action(
          onError = {
            launch {
              error set it.toString()
              delay(1000)
              nullableError set null
            }
          }
        ) {
          val file = pickFromGallery()
          files transform { it + file }
        }
      ),
      photo = ButtonIcon(
        icon = Icon.Photo,
        onClick = action(
          onError = {
            launch {
              nullableError set it.toString()
              delay(1000)
              nullableError set null
            }
          }
        ) {
          val file = takePhoto()
          files transform { it + file }
        }
      ),
      audio = ButtonIcon(
        icon = Icon.Record,
        onClick = action(
          onError = {
            launch {
              Log.e("ERROR", it.toString())
              error set it.toString()
              delay(1000)
              nullableError set null
            }
          }
        ) {
          if (isRecording) {
            bottomBar.audio.icon set Icon.Record
            val audio = stopRecording()
            playAudio(audio)
            val text = transcript(audio)
            input.text.value transform { "$it ${text.text}" }
          } else {
            bottomBar.audio.icon set Icon.CancelRecord
            startRecording()
          }
        }
      ),
      translation = ButtonIcon(
        icon = Icon.Translate,
        onClick = action(
          onError = {
            launch {
              Log.e("ERROR", it.toString())
              error set it.toString()
              delay(1000)
              nullableError set null
            }
          }
        ) {
          if (isRecording) {
            bottomBar.audio.icon set Icon.Record
            val audio = stopRecording()
            playAudio(audio)
            val text = translation(audio)
            input.text.value transform { "$it ${text.text}" }
          } else {
            bottomBar.audio.icon set Icon.CancelRecord
            startRecording()
          }
        }
      ),
      send = ButtonIcon(
        icon = Icon.Add,
        onClick = action(
          onError = {
            Log.e("ERROR", it.toString())
            launch {
              bottomBar.send.icon set Icon.Add
              nullableError set it.toString()
              delay(1000)
              nullableError set null
            }
          }
        ) {
          bottomBar.send.icon set Icon.Back
          state set State.Thinking
          input.enabled set false

          val assistant = chat(
            prompt = input.text.value.get(),
            files = files.get().toNonEmptyListOrNull(),
          )

          messages transform { messages ->
            messages + Message(
              label = Text(value = "Me", style = Label, color = Color.Blue),
              text = Text(value = input.text.value.get())
            ) + Message(
              label = Text(value = "Assistant", style = Label, color = Color.Blue),
              text = Text(value = assistant)
            )
          }

          scrollState.position set messages.get().size
          state set State.Idle
          bottomBar.send.icon set Icon.Add
          files set emptyList()
          input.text.value set ""
          input.enabled set true
        }
      ),
    ),
    messages = listOf(),
    input = BasicInput(
      text = Text(""),
      onChange = { input.text.value set it },
      placeholder = Text(
        value = "What are you thinking?",
        style = Label,
        color = Color.BlueNavy
      )
    ),
    state = State.Idle,
    scrollState = ScrollState(0, 0),
    init = {
      launch { load() }
    },
    update = {
      launch { load() }
    },
    stop = {
      coroutineContext.cancelChildren()
    }
  )
}

@Composable
operator fun Home.invoke() {
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }
  val lazyListState = rememberLazyListState(
    initialFirstVisibleItemIndex = scrollState.position,
    initialFirstVisibleItemScrollOffset = scrollState.offset
  )

  LaunchedEffect(Unit) {
    init()
  }

  LaunchedEffect(error) {
    error?.let { snackbarHostState.showSnackbar(it) } ?: snackbarHostState.currentSnackbarData?.dismiss()
  }
  val focusRequester = remember { FocusRequester() }

  LaunchedEffect(lazyListState) {
    snapshotFlow { lazyListState.layoutInfo.totalItemsCount }.collect {
      lazyListState.animateScrollToItem(it)
      focusRequester.requestFocus()
    }
  }

  Scaffold(
    topBar = { topBar() },
    bottomBar = { bottomBar() },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier.padding(innerPadding),
      state = lazyListState,
    ) {
      items(messages) { message ->
        message()
      }
      item(key = "input") {
        Column(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
          androidx.compose.material3.Text("Me")
          if (files.isNotEmpty()) {
            Spacer(Modifier.size(8.dp))
            Row(horizontalArrangement = spacedBy(8.dp)) {
              files.forEach { file ->
                var uri by remember { mutableStateOf<Uri?>(null) }
                LaunchedEffect(file) {
                  effect { file.toUri(context) }.fold({ uri = null }, { uri = it })
                }
                uri?.let {
                  Image(
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop,
                    painter = rememberAsyncImagePainter(uri, onState = { }),
                    contentDescription = null
                  )
                }
              }
            }
          }
          Spacer(Modifier.size(8.dp))
        }
        input(modifier = Modifier.focusRequester(focusRequester))
      }
    }
  }
}