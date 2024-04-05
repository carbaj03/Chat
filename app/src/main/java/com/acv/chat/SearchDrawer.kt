package com.acv.chat

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.acv.chat.components.outlineColor

@Composable
fun SearchDrawer(
  onMenuClick: () -> Unit,
  onSettingsClick: () -> Unit,
  modifier: Modifier = Modifier
) {
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

  Column(
    modifier = modifier.fillMaxSize()
  ) {
    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clip(CircleShape)
        .clickable { onMenuClick() },
      colors = outlineColor,
      value = "",
      onValueChange = { },
      shape = CircleShape,
      enabled = false,
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = null
        )
      },
      placeholder = {
        Text("Search")
      },
    )

    Spacer(Modifier.height(8.dp))

    LazyColumn(
      modifier = Modifier.weight(1f)
    ) {
      items(gpts) {
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
        HorizontalDivider()
      }

      items(history) {
        Column(
          modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
          Text(
            text = it.title
          )
//          Text(
//            text = it.subtitle,
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.Gray
//          )
        }
      }

    }

    HorizontalDivider()

    Row(
      modifier = Modifier.clickable { onSettingsClick() }.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(
        modifier = Modifier.size(40.dp).clip(CircleShape),
        painter = painterResource(R.drawable.avatar),
        contentDescription = null
      )

      Spacer(modifier = Modifier.width(16.dp))

      Text(
        text = "Alejandro Carbajo",
        modifier = Modifier.weight(1f)
      )

      Icon(
        imageVector = Icons.Default.MoreVert,
        contentDescription = null
      )
    }
  }
}
