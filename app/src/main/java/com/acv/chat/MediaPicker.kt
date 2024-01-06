package com.acv.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MediaPicker(
  expanded: Boolean,
  onClick: () -> Unit,
  onCameraClick: () -> Unit,
  onGalleryClick: () -> Unit,
  onFilesClick: () -> Unit,
) {
  Surface(
    color = MaterialTheme.colorScheme.surface,
  ) {
    AnimatedContent(
      targetState = expanded,
      transitionSpec = { customAnimation() },
      label = ""
    ) { targetExpanded ->
      if (targetExpanded) {
        Row {
          IconButton(onClick = onCameraClick) {
            Icon(painter = painterResource(R.drawable.ic_add_a_photo_24), contentDescription = null)
          }
          IconButton(onClick = onGalleryClick) {
            Icon(painter = painterResource(R.drawable.ic_image_search), contentDescription = null)
          }
          IconButton(onClick = onFilesClick) {
            Icon(painter = painterResource(R.drawable.ic_folder_24), contentDescription = null)
          }
        }
      } else {
        SmallFloatingActionButton(
          onClick = onClick,
          elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
          containerColor = MaterialTheme.colorScheme.tertiaryContainer,
          shape = CircleShape,
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
          )
        }
      }
    }
  }
}