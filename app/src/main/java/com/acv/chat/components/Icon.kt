package com.acv.chat.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.acv.chat.R

enum class Icon {
  Record, CancelRecord, Play, StopPlay, Add, AddDisabled, Back, Calendar, Time, Gallery, Photo, Send, Translate
}

@Composable
operator fun Icon.invoke() {
  when (this) {
    Icon.Record -> Icon(painterResource(id = R.drawable.ic_mic), contentDescription = null, tint = Color.Teal())
    Icon.CancelRecord -> Icon(painterResource(id = R.drawable.ic_mic_off), contentDescription = null, tint = Color.Teal())
    Icon.Translate -> Icon(painterResource(id = R.drawable.ic_translate_24), contentDescription = null, tint = Color.Teal())
    Icon.Play -> Icon(painterResource(id = R.drawable.ic_volume_off_24), contentDescription = null, tint = Color.Teal())
    Icon.StopPlay -> Icon(painterResource(id = R.drawable.ic_volume_off_24), contentDescription = null, tint = Color.Teal())
    Icon.Add -> Icon(modifier = Modifier.size(38.dp), imageVector = Icons.Filled.AddCircle, contentDescription = null, tint = Color.Black())
    Icon.AddDisabled -> Icon(modifier = Modifier.size(38.dp), imageVector = Icons.Filled.AddCircle, contentDescription = null, tint = Color.Black().copy(alpha = 0.5f))
    Icon.Back -> Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
    Icon.Calendar -> Icon(painterResource(id = R.drawable.ic_calendar_month_24), contentDescription = null)
    Icon.Time -> Icon(painterResource(id = R.drawable.ic_time_filled_24), contentDescription = null)
    Icon.Gallery -> Icon(painterResource(id = R.drawable.ic_image_search), contentDescription = null)
    Icon.Photo -> Icon(painterResource(id = R.drawable.ic_add_a_photo_24), contentDescription = null)
    Icon.Send -> Icon(modifier = Modifier.size(38.dp), imageVector = Icons.Filled.Send, contentDescription = null, tint = Color.Black())
  }
}