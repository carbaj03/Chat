package com.acv.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.acv.chat.domain.Media

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenDialog(
  onClose: () -> Unit,
  file: Media? = null,
) {
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
      file?.let {
        Image(
          modifier = Modifier.fillMaxSize(),
          painter = rememberAsyncImagePainter(it),
          contentDescription = null
        )
      }
    }
  }
}