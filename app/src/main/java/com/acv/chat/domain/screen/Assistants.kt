package com.acv.chat.domain.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import arrow.optics.optics
import com.acv.chat.arrow.error.launchEffect
import com.acv.chat.arrow.optics.invoke
import com.acv.chat.components.Text
import com.acv.chat.components.invoke
import com.acv.chat.data.openai.assistant.AssistantApi
import com.acv.chat.data.openai.assistant.runs.AssistantId
import com.acv.chat.domain.App
import com.acv.chat.domain.AppOptics
import com.acv.chat.domain.Store
import com.acv.chat.domain.screen

@optics
data class Assistants(
  val assistants: List<Assistant>,
  override val destroy: () -> Unit
) : Screen {
  companion object
}

@optics
data class Assistant(
  val id: AssistantId,
  val text: Text
) {
  companion object
}

context(AppOptics, Store<App>, AssistantApi)
fun AssistantsScreen(): Assistants =
  screen.assistants {

    launchEffect {
      assistants set assistants().data.map { Assistant(id = it.id, text = Text(it.name)) }
    }

    Assistants(
      assistants = listOf(),
      destroy = { }
    )
  }

@Composable
operator fun Assistants.invoke() {
  LazyColumn {
    items(assistants) { assistant ->
      assistant.text()
    }
  }
}