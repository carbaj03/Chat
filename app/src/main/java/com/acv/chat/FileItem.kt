package com.acv.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.acv.chat.domain.Media

@Composable
fun FileItem(
  file: Media,
  onRemove: () -> Unit,
  onClick: () -> Unit
) {
  Box {
    when (file) {
      is Media.Image -> {
        Image(
          modifier = Modifier
            .clickable { onClick() }
            .size(75.dp)
            .clip(RoundedCornerShape(8.dp)),
          painter = rememberAsyncImagePainter(file.file),
          contentDescription = null
        )
      }
      is Media.Pdf -> {
        Image(
          modifier = Modifier
            .clickable { onClick() }
            .size(75.dp)
            .clip(RoundedCornerShape(8.dp)),
          painter = painterResource(R.drawable.chatgpt_logo),
          contentDescription = null
        )
      }
    }

    IconButton(
      onClick = onRemove,
      modifier = Modifier
        .align(Alignment.TopEnd)
        .size(28.dp)
    ) {
      Icon(
        painter = painterResource(R.drawable.ic_close_24),
        contentDescription = null,
        modifier = Modifier
          .size(20.dp)
          .background(Color.White.copy(alpha = 0.5f), shape = CircleShape)
      )
    }
  }
}
