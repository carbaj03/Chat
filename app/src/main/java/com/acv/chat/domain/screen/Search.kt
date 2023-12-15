package com.acv.chat.domain.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun Search(
  onMenuClick: () -> Unit = {},
) {
  var more by remember { mutableStateOf(false) }

  Column {
    OutlinedTextField(
      modifier = Modifier.clickable { onMenuClick() },
      value = "",
      onValueChange = { },
      shape = CircleShape,
      enabled = false,
    )
    LazyColumn {
      items(if (more) 5 else 10) {
        Text("Item $it")
      }
      item {
        Row(modifier = Modifier.clickable { more = !more }) {
          Text("more")
        }
      }
      items(5) {
        Text("Item $it")
      }
    }
  }
}

@Composable
fun Search2(
  onBackClick: () -> Unit,
) {
  var search by remember { mutableStateOf("") }
  var more by remember { mutableStateOf(false) }

  Column(Modifier.fillMaxSize()) {
    OutlinedTextField(
      value = search,
      onValueChange = { search = it },
      shape = CircleShape,
      leadingIcon = {
        IconButton(onClick = onBackClick) {
          Icon(Icons.Default.ArrowBack, contentDescription = null)
        }
      },
    )
    LazyColumn {
      items(if (more) 5 else 10) {
        Text("Item $it")
      }
      item {
        Row(modifier = Modifier.clickable { more = !more }) {
          Text("more")
        }
      }
      items(5) {
        Text("Item $it")
      }
    }
  }
}