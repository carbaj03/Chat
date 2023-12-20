package com.acv.chat.domain.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.acv.chat.R
import com.acv.chat.util.ifNotEmpty

@Composable
fun Search(
  onMenuClick: () -> Unit,
  onSettingsClick: () -> Unit
) {
  var more by remember { mutableStateOf(true) }

  Column {
    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clip(CircleShape)
        .clickable { onMenuClick() },
      value = "",
      onValueChange = { },
      shape = CircleShape,
      enabled = false,
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = null)
      },
      placeholder = {
        Text("Search")
      },
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        disabledContainerColor = Color.LightGray,
        focusedContainerColor = Color.LightGray,
        unfocusedContainerColor = Color.LightGray,
      )
    )
    Spacer(Modifier.height(8.dp))
    LazyColumn(
      modifier = Modifier.weight(1f)
    ) {
      items(if (more) 5 else 10) {
        Item(
          text = "Item $it",
          url = "https://fastly.picsum.photos/id/423/200/200.jpg?hmac=fXwRSSVHFlYgq9MfObWaWCb_p9L6ysOWda9lLOtAWc0",
        )
      }
      item {
        Row(
          modifier = Modifier.clickable { more = !more }.padding(16.dp)
        ) {
          Text(text = if (more) "more" else "less")
          if (more)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
          else
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
        }
        Divider()
      }
      items(5) {
        when (it) {
          0 -> {
            Row(
              modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
              Text("Yesterday", style = MaterialTheme.typography.labelMedium)
            }
          }
          3 -> {
            Row(
              modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
              Text("Previous 7 days", style = MaterialTheme.typography.labelMedium)
            }
          }
          else -> {
            Row(
              modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
              Text("Item $it")
            }
          }
        }

      }
    }
    Divider()
    Row(
      modifier = Modifier.clickable { onSettingsClick() }.padding(16.dp),
      verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
      Image(
        modifier = Modifier.size(40.dp).clip(CircleShape),
        painter = painterResource(R.drawable.avatar),
        contentDescription = null
      )
      Spacer(Modifier.width(16.dp))
      Text(text = "Item", modifier = Modifier.weight(1f))
      Icon(Icons.Default.MoreVert, contentDescription = null)
    }
  }
}

@Composable
fun Search2(
  onBackClick: () -> Unit,
) {
  var search by remember { mutableStateOf("") }
  var more by remember { mutableStateOf(true) }

  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      value = search,
      onValueChange = { search = it },
      shape = CircleShape,
      leadingIcon = {
        IconButton(onClick = onBackClick) {
          Icon(Icons.Default.ArrowBack, contentDescription = null)
        }
      },
      trailingIcon = {
        search.ifNotEmpty {
          IconButton(onClick = { search = "" }) {
            Icon(Icons.Default.Clear, contentDescription = null)
          }
        }
      },
      placeholder = {
        Text("Search")
      },
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        disabledContainerColor = Color.LightGray,
        focusedContainerColor = Color.LightGray,
        unfocusedContainerColor = Color.LightGray,
      )
    )
    Spacer(Modifier.height(8.dp))
    LazyColumn {
      items(if (more) 5 else 10) {
        Item(
          text = "Item $it",
          url = "https://fastly.picsum.photos/id/423/200/200.jpg?hmac=fXwRSSVHFlYgq9MfObWaWCb_p9L6ysOWda9lLOtAWc0"
        )
      }
      item {
        Row(
          modifier = Modifier.clickable { more = !more }.padding(16.dp)
        ) {
          Text(text = if (more) "more" else "less")
          if (more)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
          else
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
        }
        Divider()
      }
      items(5) {
        Column(
          modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
          Text("Item $it")
          Text("asdf", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
      }
    }
  }
}

@Composable
fun Item(text: String, url: String, modifier: Modifier = Modifier) {
  Row(
    modifier = modifier.padding(16.dp).fillMaxWidth()
  ) {
    Image(
      modifier = Modifier.size(28.dp).clip(CircleShape),
      painter = rememberAsyncImagePainter(url),
      contentDescription = null
    )
    Spacer(Modifier.width(16.dp))
    Text(text)
  }
}