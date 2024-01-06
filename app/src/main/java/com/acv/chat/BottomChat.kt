package com.acv.chat

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import arrow.core.raise.either
import com.acv.chat.components.LocalColorScheme
import com.acv.chat.components.invoke
import com.acv.chat.components.surfaceVariant
import com.acv.chat.domain.Media
import com.acv.chat.domain.rememberDocumentLauncher
import com.acv.chat.domain.rememberGalleryLauncher
import com.acv.chat.domain.rememberPhotoLauncher
import com.acv.chat.util.TempFileProvider
import com.acv.chat.util.rememberFileProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun BottomChat(
  isMultimodal: Boolean,
  onSend: (String, List<Media>) -> Unit,
  onAssistant: () -> Unit,
  onSpeech: () -> Unit,
  speech: Boolean,
  fileClick: (Media) -> Unit,
  modifier: Modifier = Modifier
) {
  val scope = rememberCoroutineScope()
  var text: String by rememberSaveable { mutableStateOf("") }
  var files: List<Media> by rememberSaveable(stateSaver = FilesSaver) { mutableStateOf(listOf()) }
  var sending: Boolean by remember { mutableStateOf(false) }
  var expanded by remember { mutableStateOf(true) }

  val fileProvider: TempFileProvider = rememberFileProvider()
  val documentService = rememberDocumentLauncher(fileProvider)
  val photoService = rememberPhotoLauncher(fileProvider)
  val galleryService = rememberGalleryLauncher(fileProvider)

  val actionResource = when {
    sending -> R.drawable.ic_stop_24
    text.isEmpty() -> R.drawable.ic_headphones_24
    else -> R.drawable.ic_arrow_upward_24
  }

  val actionOnClick: () -> Unit = {
    when {
      sending -> {
        sending = false
      }
      text.isNotEmpty() -> {
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
    }
  }

  Column {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = modifier.padding(8.dp)
    ) {
      if (isMultimodal) {
        MediaPicker(
          expanded = expanded,
          onClick = { expanded = true },
          onCameraClick = {
            scope.launch {
              either {
                val file = galleryService.pickFromGallery()
                expanded = false
                files = files + file
              }
            }
          },
          onFilesClick = {
            scope.launch {
              either {
                val file = documentService.pickDocument()
                expanded = false
                files = files + file
              }
            }
          },
          onGalleryClick = {
            scope.launch {
              either {
                val file = photoService.takePhoto()
                expanded = false
                files = files + file
              }
            }
          }
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Box(
        modifier = Modifier.weight(1f),
      ) {
        Column(
          modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(28.dp))
            .padding(vertical = 8.dp, horizontal = 12.dp),
          verticalArrangement = Arrangement.Center
        ) {

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            items(files) {
              FileItem(
                file = it,
                onRemove = { files = files.dropLast(1) },
                onClick = { fileClick(it) }
              )
            }
          }

          Row(
            modifier = Modifier
              .padding(8.dp)
              .fillMaxWidth()
              .heightIn(min = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Box(
              modifier = Modifier.weight(1f),
              contentAlignment = Alignment.CenterStart
            ) {

              BasicTextField(
                value = text,
                onValueChange = {
                  text = it
                  expanded = false
                },
                interactionSource = rememberInteractionSource(
                  onPress = { expanded = false }
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),
                enabled = !speech,
                textStyle = MaterialTheme.typography.bodyLarge.copy(MaterialTheme.colorScheme.onSurfaceVariant),
              )
              if (text.isEmpty())
                Text(
                  text = "Message",
                  style = MaterialTheme.typography.bodyLarge,
                  color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }

            when {
              sending -> {
                CircularProgressIndicator(
                  modifier = Modifier.size(20.dp),
                  strokeWidth = 1.dp
                )
              }
              text.isEmpty() -> {
                val icon = if (speech) R.drawable.ic_mic_off else R.drawable.ic_mic
                Icon(
                  modifier = Modifier.clickable { onSpeech() },
                  painter = painterResource(icon),
                  contentDescription = null
                )
              }
            }
          }
        }
      }

//      OutlinedTextField(
//        modifier = Modifier.weight(1f),
//        colors = outlineColor,
//        interactionSource = rememberInteractionSource(
//          onPress = { expanded = false }
//        ),
//        shape = CircleShape,
//        value = text,
//        enabled = !speech,
//        onValueChange = {
//          text = it
//          expanded = false
//        },
//        placeholder = {
//          Text("Message")
//        },
//        trailingIcon = {
//          when {
//            sending -> {
//              CircularProgressIndicator(
//                modifier = Modifier.size(20.dp),
//                strokeWidth = 1.dp
//              )
//            }
//            text.isEmpty() -> {
//              IconButton(
//                onClick = onSpeech
//              ) {
//                val icon = if (speech) R.drawable.ic_mic_off else R.drawable.ic_mic
//                Icon(
//                  painter = painterResource(icon),
//                  contentDescription = null
//                )
//              }
//            }
//          }
//        }
//      )

      Spacer(modifier = Modifier.width(8.dp))

      SmallFloatingActionButton(
        onClick = actionOnClick,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
      ) {
        Icon(
          painter = painterResource(actionResource),
          contentDescription = null
        )
      }
    }
  }
}