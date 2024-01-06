package com.acv.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.acv.chat.domain.Media

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatRow(
  msg: Message,
  fileClick: (Media) -> Unit
) {
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
            FlowRow(
              maxItemsInEachRow = 3,
              verticalArrangement = Arrangement.spacedBy(8.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              msg.files.forEach {
                when (it) {
                  is Media.Image -> Image(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).size(75.dp).clickable { fileClick(it) },
                    painter = rememberAsyncImagePainter(it.file),
                    contentDescription = null
                  )
                  is Media.Pdf -> Image(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).size(75.dp).clickable { fileClick(it) },
                    painter = painterResource(R.drawable.chatgpt_logo),
                    contentDescription = null
                  )
                }
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
fun Preview() {
  Surface {
    ChatRow(
      msg = Message.Human(
        text = "sdfa",
        files = listOf(Media.Pdf(""))
      ),
      fileClick = {}
    )
  }
}
