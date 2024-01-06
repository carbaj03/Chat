package com.acv.chat

import androidx.compose.runtime.saveable.Saver
import com.acv.chat.domain.Media
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val FilesSaver = Saver<List<Media>, String>(
  save = { messages ->
    Json.encodeToString(messages)
  },
  restore = { maps ->
    Json.decodeFromString<List<Media>>(maps)
  }
)


val MessagesSaver = Saver<List<Message>, String>(
  save = { messages ->
    Json.encodeToString(messages)
  },
  restore = { maps ->
    Json.decodeFromString<List<Message>>(maps)
  }
)
