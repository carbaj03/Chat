package com.acv.chat.domain.screen

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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Composable
fun Chat(navigate: (String) -> Unit) {

  val drawerState = rememberDrawerState(DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      DismissibleDrawerSheet {
        Menu(enabled = false, onMenuClick = { navigate("search") })
      }
    },
    content = {
      Content(
        onMenuClick = {
          scope.launch { drawerState.open() }
        }
      )
    }
  )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Menu(enabled: Boolean, onMenuClick: () -> Unit) {
//  SearchBar(
//    onQueryChange = {},
//    query = "",
//    onSearch = {},
//    active = true,
//    onActiveChange = {},
//  ) {
//
//  }
  Column {
    Search(onMenuClick = onMenuClick)
    Row(Modifier.padding(16.dp)) {
      Text("Item")
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(onMenuClick: () -> Unit) {
  var text by remember { mutableStateOf("") }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = { Text("ChatGPT 4") },
        navigationIcon = {
          IconButton(onClick = onMenuClick) {
            Icon(Icons.Default.Menu, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(onClick = {}) {
            Icon(Icons.Default.Menu, contentDescription = "Back")
          }
          IconButton(onClick = {}) {
            Icon(Icons.Default.Menu, contentDescription = "Back")
          }
        }
      )
    },
    bottomBar = {
      Column {
        var expanded by remember { mutableStateOf(false) }
        var speech by remember { mutableStateOf(false) }

        BottomAppBar {
          Surface(
            color = MaterialTheme.colorScheme.primary,
            onClick = { expanded = !expanded }
          ) {
            AnimatedContent(
              targetState = expanded,
              transitionSpec = {
                fadeIn(animationSpec = tween(150, 150)) togetherWith
                        fadeOut(animationSpec = tween(150)) using
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
                  IconButton(onClick = { expanded = false }) {
                    Icon(Icons.Default.Add, contentDescription = "Back")
                  }
                  IconButton(onClick = { expanded = false }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                  }
                  IconButton(onClick = { expanded = false }) {
                    Icon(Icons.Default.Home, contentDescription = "Back")
                  }
                }
              } else {
                IconButton(onClick = { expanded = true }) {
                  Icon(Icons.Default.Add, contentDescription = "Back")
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
          OutlinedTextField(
            modifier = Modifier.weight(1f),
            interactionSource = interactionSource,
            shape = CircleShape,
            value = text,
            onValueChange = { text = it; expanded = false },
            placeholder = { Text("Message") },
            trailingIcon = {
              if (text.isEmpty()) {
                IconButton(onClick = { speech = !speech }) {
                  if (speech)
                    Icon(Icons.Default.Close, contentDescription = "Back")
                  else
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Back")
                }
              }
            }
          )
          IconButton(onClick = {}) {
            Icon(if (text.isEmpty()) Icons.Default.ArrowDropDown else Icons.Default.Send, contentDescription = "Back")
          }
        }

        AnimatedVisibility(speech) {
          var state by remember { mutableStateOf("normal") }
          when (state) {
            "error" -> {
              Box(modifier = Modifier.height(200.dp).fillMaxWidth().background(MaterialTheme.colorScheme.primary)) {
                IconButton(onClick = { speech = false }, modifier = Modifier.align(Alignment.TopEnd)) {
                  Icon(Icons.Default.Close, contentDescription = "Back")
                }
                Column(modifier = Modifier.align(Alignment.Center)) {
                  Text(text = "No speech detected")
                  Button(onClick = { state = "normal" }) {
                    Text("Start new recording")
                  }
                }
              }
            }
            else -> {
              Box(
                modifier = Modifier
                  .height(200.dp)
                  .fillMaxWidth()
                  .clickable {
                    state = "error"
                  }
              ) {
                var counter by remember { mutableIntStateOf(0) }
                val infiniteTransition = rememberInfiniteTransition()
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
                    counter = counter++
                  }
                }
                Text(counter.toString())
                Box(modifier = Modifier.size(size).align(Alignment.Center).background(MaterialTheme.colorScheme.primary, shape = CircleShape))
                Text(modifier = Modifier.align(Alignment.Center), text = "Talk")
              }
            }
          }
        }
      }
    }
  ) {
    LazyColumn(modifier = Modifier.padding(it)) {
      items(100) {
        Row {
          Image(
            modifier = Modifier.padding(16.dp),
            imageVector = Icons.Default.Menu,
            contentDescription = "Back"
          )
          Column {
            Text("Item $it")
            Text("Item $it")
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun ChatPreview() {
  Chat(navigate = {})
}