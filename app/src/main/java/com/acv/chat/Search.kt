package com.acv.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.acv.chat.components.outlineColor
import com.acv.chat.util.ifNotEmpty

data class Gpts(
  val name: String,
  val url: String
)

data class History(
  val title: String,
  val subtitle: String
)

@Composable
fun SearchScreen(
  onBackClick: () -> Unit,
) {
  var search by remember { mutableStateOf("") }
  var more by remember { mutableStateOf(true) }

  val gpts = listOf(
    Gpts("DALL-E", "https://fastly.picsum.photos/id/423/200/200.jpg?hmac=fXwRSSVHFlYgq9MfObWaWCb_p9L6ysOWda9lLOtAWc0"),
    Gpts("Data Analysis", "https://fastly.picsum.photos/id/423/200/200.jpg?hmac=fXwRSSVHFlYgq9MfObWaWCb_p9L6ysOWda9lLOtAWc0"),
    Gpts("Web Browswe", "https://fastly.picsum.photos/id/423/200/200.jpg?hmac=fXwRSSVHFlYgq9MfObWaWCb_p9L6ysOWda9lLOtAWc0"),
  )
  val history = listOf(
    History("Thumbnail for a youtube video", "asdfdsf"),
    History("Thumbnail for a youtube video", "asdfdsf"),
    History("Thumbnail for a youtube video", "asdfdsf"),
    History("Thumbnail for a youtube video", "asdfdsf"),
  )

  val gptsFiltered by remember(search, more) {
    derivedStateOf {
      gpts.filter { it.name.contains(search, ignoreCase = true) }.let {
        if (!more) {
          more = false; it.take(2)
        } else it
      }
    }
  }
  val historyFiltered by remember(search) { derivedStateOf { history.filter { it.title.contains(search, ignoreCase = true) } } }

  Surface(
    modifier = Modifier.background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier.fillMaxSize()
    ) {

      OutlinedTextField(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        value = search,
        onValueChange = { search = it },
        shape = CircleShape,
        leadingIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.Filled.ArrowBack, contentDescription = null)
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
        colors = outlineColor
      )

      Spacer(Modifier.height(8.dp))

      LazyColumn {

        items(gptsFiltered) {
          Item(
            text = it.name,
            url = it.url
          )
        }

        item {
          Row(
            modifier = Modifier.clickable { more = !more }.padding(16.dp)
          ) {
            Text(text = if (more) "more" else "less")

            Icon(
              imageVector = if (more) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
              contentDescription = null
            )
          }
        }

        if (historyFiltered.isNotEmpty())
          item {
            HorizontalDivider()
          }

        if (history.isNotEmpty()) {

          items(historyFiltered) {
            Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
              Text(
                text = it.title
              )
              Text(
                text = it.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
              )
            }
          }
        } else {
          item {
            Text(
              modifier = Modifier.padding(16.dp).fillMaxSize(),
              text = "No results found",
              style = MaterialTheme.typography.bodySmall,
              color = Color.Gray
            )
          }
        }
      }
    }
  }
}

@Composable
fun Item(
  text: String,
  url: String,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.padding(16.dp).fillMaxWidth()
  ) {
    Image(
      modifier = Modifier.size(28.dp).clip(CircleShape),
      painter = rememberAsyncImagePainter(url),
      contentDescription = null
    )
    Spacer(Modifier.width(16.dp))
    Text(text = text)
  }
}