package com.acv.chat.domain.screen

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import com.acv.chat.R
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Composable
fun Chat(
  onMenuClick: () -> Unit,
  onSettingsClick: () -> Unit
) {

  val drawerState = rememberDrawerState(DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      DismissibleDrawerSheet {
        Search(
          onMenuClick = onMenuClick,
          onSettingsClick = onSettingsClick
        )
      }
    }
  ) {
    Content(
      onMenuClick = {
        scope.launch { drawerState.open() }
      }
    )
  }
}

@Composable
fun BottomChat(
  isMultimodal: Boolean,
  onSend: (String, List<String>) -> Unit,
  onAssistant: () -> Unit,
) {
  val scope = rememberCoroutineScope()
  var text by rememberSaveable { mutableStateOf("") }
  var files: List<String> by remember { mutableStateOf(listOf()) }
  var sending by remember { mutableStateOf(false) }

  Column {
    var expanded by remember { mutableStateOf(false) }
    var speech by remember { mutableStateOf(false) }
    BottomAppBar(
      tonalElevation = 0.dp,
    ) {
      if (isMultimodal)
        Surface(
          color = MaterialTheme.colorScheme.surface,
          onClick = { expanded = !expanded }
        ) {
          AnimatedContent(
            targetState = expanded,
            transitionSpec = {
              fadeIn(animationSpec = spring()) togetherWith
                      fadeOut(animationSpec = spring()) using
                      SizeTransform { initialSize, targetSize ->
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
            },
            label = ""
          ) { targetExpanded ->
            if (targetExpanded) {
              Row {
                IconButton(onClick = {
                  expanded = false
                  files = files + "photo"
                }) {
                  Icon(painter = painterResource(R.drawable.ic_add_a_photo_24), contentDescription = null)
                }
                IconButton(onClick = {
                  expanded = false
                  files = files + "image"
                }) {
                  Icon(painter = painterResource(R.drawable.ic_image_search), contentDescription = null)
                }
                IconButton(onClick = {
                  expanded = false
                  files = files + "pdf"
                }) {
                  Icon(painter = painterResource(R.drawable.ic_folder_24), contentDescription = null)
                }
              }
            } else {
              SmallFloatingActionButton(
                onClick = { expanded = true },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                shape = CircleShape,
              ) {
                Icon(Icons.Default.Add, contentDescription = null)
              }
            }
          }
        }
      val interactionSource = remember {
        object : MutableInteractionSource {
          override val interactions = MutableSharedFlow<Interaction>(
            extraBufferCapacity = 16,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
          )

          override suspend fun emit(interaction: Interaction) {
            if (interaction is PressInteraction.Press) {
              expanded = false
            }

            interactions.emit(interaction)
          }

          override fun tryEmit(interaction: Interaction): Boolean {
            return interactions.tryEmit(interaction)
          }
        }
      }
      Spacer(modifier = Modifier.width(8.dp))
      OutlinedTextField(
        modifier = Modifier.weight(1f),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Transparent,
          unfocusedBorderColor = Transparent,
          disabledContainerColor = LightGray,
          focusedContainerColor = LightGray,
          unfocusedContainerColor = LightGray,
        ),
        interactionSource = interactionSource,
        shape = CircleShape,
        value = text,
        enabled = !speech,
        onValueChange = {
          text = it
          expanded = false
        },
        placeholder = { Text("Message") },
        trailingIcon = {
          if (sending) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
          } else if (text.isEmpty()) {
            IconButton(onClick = { speech = !speech }) {
              if (speech)
                Icon(painter = painterResource(R.drawable.ic_mic_off), contentDescription = null)
              else
                Icon(painter = painterResource(R.drawable.ic_mic), contentDescription = null)
            }
          }
        }
      )
      Spacer(modifier = Modifier.width(8.dp))
      SmallFloatingActionButton(
        onClick = {
          if (sending) {
            sending = false
          } else if (text.isNotEmpty()) {
            scope.launch {
              sending = true
              onSend(text, files)
              files = emptyList()
              text = ""
              delay(3000)
              sending = false
              onAssistant()
            }
          }
        },
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        shape = CircleShape,
        containerColor = Color.Black,
        contentColor = Color.White
      ) {
        Icon(
          painter = painterResource(if (sending) R.drawable.ic_stop_24 else if (text.isEmpty()) R.drawable.ic_headphones_24 else R.drawable.ic_arrow_upward_24),
          contentDescription = null
        )
      }
    }

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(bottom = 8.dp),
    ) {
      items(files) {
        Box {
          Image(
            modifier = Modifier
              .size(75.dp)
              .clip(RoundedCornerShape(8.dp)),
            painter = painterResource(R.drawable.avatar),
            contentDescription = null
          )
          IconButton(
            onClick = { files = files.dropLast(1) },
            modifier = Modifier.align(Alignment.TopEnd).size(28.dp)
          ) {
            Icon(
              painter = painterResource(R.drawable.ic_close_24),
              contentDescription = null,
              modifier = Modifier.size(20.dp).background(Color.White.copy(alpha = 0.5f), shape = CircleShape)
            )
          }
        }
      }
    }

    AnimatedVisibility(speech) {
      var state by remember { mutableStateOf("normal") }
      when (state) {
        "error" -> {
          Box(
            modifier = Modifier.height(200.dp).fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer)
          ) {
            IconButton(
              onClick = { speech = false },
              modifier = Modifier.align(Alignment.TopEnd)
            ) {
              Icon(Icons.Default.Close, contentDescription = null)
            }
            Column(
              modifier = Modifier.align(Alignment.Center)
            ) {
              Text(
                text = "No speech detected",
                modifier = Modifier.align(Alignment.CenterHorizontally)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { state = "normal" },
                colors = ButtonDefaults.buttonColors(
                  containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                )
              ) {
                Text("Start new recording")
              }
            }
          }
        }
        "loading" -> {
          Box(
            modifier = Modifier.height(200.dp).fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer)
          ) {
            Row(
              modifier = Modifier.align(Alignment.Center),
              verticalAlignment = Alignment.CenterVertically
            ) {
              CircularProgressIndicator()
              Spacer(modifier = Modifier.width(8.dp))
              Text(text = "Converting to text...")
            }
          }
        }
        else -> {
          Box(
            modifier = Modifier
              .clickable {
                scope.launch {
                  state = "loading"
                  delay(1000)
                  state = "error"
                }
              }
              .background(MaterialTheme.colorScheme.primaryContainer)
              .height(200.dp)
              .padding(16.dp)
              .fillMaxWidth()
          ) {
            var counter by remember { mutableIntStateOf(0) }
            val infiniteTransition = rememberInfiniteTransition(label = "")
            val size by infiniteTransition.animateValue(
              initialValue = 40.dp,
              targetValue = 70.dp,
              Dp.VectorConverter,
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
            Text(counter.toHoursMinuteSeconds())
            Box(
              modifier = Modifier
                .size(size)
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
            )
            Text(
              text = "Talk",
              modifier = Modifier.align(Alignment.Center),
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarChat(
  title: String,
  onMenuClick: () -> Unit,
  onViewDetailsClick: () -> Unit,
  onNewChatClick: () -> Unit,
  model: String,
  onChangeModelClick: (String) -> Unit,
  isNew: Boolean
) {
  TopAppBar(
    title = { Text(title) },
    navigationIcon = {
      IconButton(onClick = onMenuClick) {
        Icon(Icons.Default.Menu, contentDescription = null)
      }
    },
    actions = {
      IconButton(
        onClick = onNewChatClick,
        enabled = !isNew
      ) {
        Icon(painter = painterResource(R.drawable.ic_open_in_new_24), contentDescription = null)
      }
      Box(modifier = Modifier.align(Alignment.CenterVertically)) {
        var menu by remember { mutableStateOf(false) }

        DropdownMenu(
          expanded = menu,
          onDismissRequest = { menu = false },
          properties = PopupProperties(focusable = false)
        ) {
          if (isNew) {
            OptionItem(
              text = "View Details",
              onClick = {
                onViewDetailsClick()
                menu = false
              },
              selected = false,
              icon = R.drawable.ic_info_outline_24
            )
            Divider()
            OptionItem(
              text = "GPT-3.5",
              onClick = {
                onChangeModelClick("ChatGPT 3.5")
                menu = false
              },
              selected = model == "ChatGPT 3.5",
              icon = R.drawable.ic_voice
            )
            OptionItem(
              text = "GPT-4",
              onClick = {
                onChangeModelClick("ChatGPT 4")
                menu = false
              },
              selected = model == "ChatGPT 4",
              icon = R.drawable.ic_voice
            )
          } else {
            OptionItem(
              text = "View Details",
              onClick = {
                onViewDetailsClick()
                menu = false
              },
              selected = false,
              icon = R.drawable.ic_info_outline_24
            )
            OptionItem(
              text = "Share",
              onClick = { menu = false },
              selected = false,
              icon = R.drawable.ic_share_24
            )
            Divider()
            OptionItem(
              text = "Rename",
              onClick = { menu = false },
              selected = false,
              icon = R.drawable.ic_edit_24
            )
            OptionItem(
              text = "Delete",
              onClick = { menu = false },
              selected = false,
              icon = R.drawable.ic_delete_outline_24
            )
          }
        }
        IconButton(onClick = { menu = !menu }) {
          Icon(Icons.Default.MoreVert, contentDescription = null)
        }
      }
    }
  )
}

@Composable
fun OptionItem(
  text: String,
  onClick: () -> Unit,
  selected: Boolean,
  @DrawableRes icon: Int,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .clickable { onClick() }
      .padding(16.dp)
      .fillMaxWidth()
  ) {
    Icon(
      painter = painterResource(icon),
      contentDescription = null,
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(text = text)
    if (selected) {
      Spacer(modifier = Modifier.weight(1f))
      Icon(
        painter = painterResource(R.drawable.ic_check_24),
        contentDescription = null,
      )
    }
  }
}

sealed interface Message {
  val text: String

  data class Human(override val text: String, val files: List<String> = emptyList()) : Message
  data class Assistant(override val text: String) : Message
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenDialog(onClose: () -> Unit) {
  Dialog(
    properties = DialogProperties(usePlatformDefaultWidth = false),
    onDismissRequest = onClose
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        TopAppBar(
          title = { },
          navigationIcon = {
            IconButton(onClick = onClose) {
              Icon(Icons.Default.Close, contentDescription = null)
            }
          },
        )
      }
    ) {
      Text(text = "Hello")
    }

  }
}

@Composable
fun Content(
  onMenuClick: () -> Unit
) {

  var items: List<Message> by rememberSaveable { mutableStateOf(listOf()) }
  val scope = rememberCoroutineScope()
  var showDialog by remember { mutableStateOf(false) }
  val lazyListState = rememberLazyListState()
  var model by remember { mutableStateOf("ChatGPT 4") }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopBarChat(
        title = model,
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
        isMultimodal = model == "ChatGPT 4",
        onSend = { msg, files -> items = items + Message.Human(msg, files) },
        onAssistant = {
          scope.launch {
            items = items + Message.Assistant("")
            "Hello world from the other side of the world ".forEach {
              delay(70)
              items = items.toMutableList().apply { set(items.size - 1, Message.Assistant(items.last().text + it)) }
            }
          }
        }
      )
    }
  ) {
    LaunchedEffect(lazyListState) {
      snapshotFlow { lazyListState.layoutInfo.totalItemsCount }.collect { lazyListState.scrollToItem(it) }
    }
    val end by remember(lazyListState) { derivedStateOf { lazyListState.canScrollForward } }
    Box(modifier = Modifier.padding(it).fillMaxSize()) {
      LazyColumn(state = lazyListState) {
        items(items) {
          ChatRow(it)
        }
      }
      if (items.isEmpty()) {
        Image(
          modifier = Modifier.size(52.dp).clip(CircleShape).align(Alignment.Center),
          painter = painterResource(R.drawable.chatgpt_logo),
          contentDescription = null
        )
      }
      if (end)
        SmallFloatingActionButton(
          elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
          shape = CircleShape,
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 8.dp),
          onClick = { scope.launch { lazyListState.scrollToItem(items.size - 1) } }
        ) {
          Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
    }
  }

  if (showDialog) {
    FullScreenDialog(onClose = { showDialog = false })
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatRow(msg: Message) {
  when (msg) {
    is Message.Human -> {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp)
      ) {
        Image(
          modifier = Modifier.size(20.dp).clip(CircleShape),
          painter = painterResource(R.drawable.avatar),
          contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "You",
            style = MaterialTheme.typography.labelMedium
          )
          Column {
            FlowRow(maxItemsInEachRow = 3, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              msg.files.forEach {
                Image(
                  modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                  painter = painterResource(R.drawable.avatar),
                  contentDescription = null
                )
              }
            }

            Text(
              text = msg.text,
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }
      }
    }
    is Message.Assistant -> {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp)
      ) {
        Image(
          modifier = Modifier.size(20.dp).clip(CircleShape),
          painter = painterResource(R.drawable.chatgpt_logo),
          contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "You",
            style = MaterialTheme.typography.labelMedium
          )
          Text(
            text = msg.text,
            style = MaterialTheme.typography.bodyMedium
          )
        }
      }
    }
  }

}

@Preview
@Composable
fun ChatPreview() {
  Chat({}, {})
}