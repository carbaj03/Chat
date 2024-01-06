package com.acv.chat.domain.screen

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import arrow.core.toNonEmptyListOrNull
import arrow.optics.optics
import coil.compose.rememberAsyncImagePainter
import com.acv.chat.arrow.error.onClick
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
import com.acv.chat.components.audioButton
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
import com.acv.chat.domain.Analytics
import com.acv.chat.domain.App
import com.acv.chat.domain.AppOptics
import com.acv.chat.domain.AudioPlayer
import com.acv.chat.domain.AudioRecorder
import com.acv.chat.domain.DocumentService
import com.acv.chat.domain.Event
import com.acv.chat.domain.Media
import com.acv.chat.domain.MediaService
import com.acv.chat.domain.Navigator
import com.acv.chat.domain.PhotoService
import com.acv.chat.domain.Store
import com.acv.chat.domain.screen
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class State {
  Searching, Thinking, Idle
}

@optics data class Home(
  val topBar: TopBar,
  val bottomBar: BottomBar,
  val messages: List<Message>,
  val files: List<Media> = emptyList(),
  val input: BasicInput,
  val error: String? = null,
  val state: State,
  val scrollState: ScrollState,
  override val destroy: () -> Unit
) : Screen {
  companion object
}

@optics data class ScrollState(
  val position: Int,
  val offset: Int,
) {
  companion object
}

context(AppOptics, Store<App>, Navigator, Analytics, PhotoService, MediaService, DocumentService, AudioPlayer, AudioRecorder, ChatService, AudioService)
fun HomeScreen(): Home = screen.home {
  launch {
    delay(1000)

    messages set listOf(
      Message(
        label = Text(value = "Assistant", style = Label, color = Color.BlueNavy),
        text = Text(value = "Hola My Friend")
      ),
    )
  }

  Event.Screen("Home").track()

  Home(
    topBar = TopBar(
      title = Text("Home")
    ),
    bottomBar = BottomBar(
      input = OutlinedInput(
        text = Text(""),
        onChange = { bottomBar.input.text.value set it },
      ),
      galleryButton = ButtonIcon(
        icon = Icon.Gallery,
        onClick = onClick(
          action = {
            val file = pickDocument()
            files transform { it + file }
          },
          onError = {
            error set it.toString()
            delay(1000)
            nullableError set null
          }
        )
      ),
      photoButton = ButtonIcon(
        icon = Icon.Photo,
        onClick = onClick(
          action = {
            val file = takePhoto()
            files transform { it + file }
          },
          onError = {
            nullableError set it.toString()
            delay(1000)
            nullableError set null
          }
        )
      ),
      audioButton = ButtonIcon(
        icon = Icon.Record,
        onClick = onClick {
          if (isRecording) {
            bottomBar.audioButton.icon set Icon.Record

            val audio = stopRecording()
            audio.play()
            val transcription = audio.transcript()
            input.text.value transform {
              if (it.isNotEmpty()) "$it ${transcription.text}"
              else transcription.text
            }
          } else {
            bottomBar.audioButton.icon set Icon.CancelRecord
            startRecording()
          }
        }
      ),
      translationButton = ButtonIcon(
        icon = Icon.Translate,
        onClick = onClick(
          action = {
            Event.Click("Translate").track()

            if (isRecording) {
              bottomBar.audioButton.icon set Icon.Record
              val audio = stopRecording()
              audio.play()
              val text = audio.translate()
              input.text.value transform { "$it ${text.text}" }
            } else {
              bottomBar.audioButton.icon set Icon.CancelRecord
              startRecording()
            }
          },
          onError = {
            error set it.toString()
            delay(1000)
            nullableError set null
          },
        )
      ),
      send = ButtonIcon(
        icon = Icon.Add,
        onClick = onClick(
          action = {
            bottomBar.send.icon set Icon.Back
            state set State.Thinking
            input.enabled set false

            val assistant = chat(
              prompt = input.text.value.get(),
              files = files.get().toNonEmptyListOrNull(),
            )

            messages transform { messages ->
              messages+ Message(
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
          },
          onError = {
            bottomBar.send.icon set Icon.Add
            nullableError set it.toString()
            delay(1000)
            nullableError set null
          }
        )
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
    destroy = { coroutineContext.cancelChildren() }
  )
}

@Composable
operator fun Home.invoke() {
  val snackbarHostState = remember { SnackbarHostState() }
  val lazyListState = rememberLazyListState(
    initialFirstVisibleItemIndex = scrollState.position,
    initialFirstVisibleItemScrollOffset = scrollState.offset
  )

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
                when (file) {
                  is Media.Image -> {
                    Image(
                      modifier = Modifier.size(64.dp).clip(RoundedCornerShape(4.dp)),
                      contentScale = ContentScale.Crop,
                      painter = rememberAsyncImagePainter(file.file, onState = {}),
                      contentDescription = null
                    )
                  }
                  is Media.Pdf -> androidx.compose.material3.Text("PDF ${file.file}")
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