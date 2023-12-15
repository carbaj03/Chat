package com.acv.chat.data.openai.assistant.runs

import com.acv.chat.data.openai.assistant.AssistantTool
import com.acv.chat.data.openai.assistant.thread.ThreadRequest
import kotlinx.serialization.Serializable

@Serializable
data class CreateThreadAndRunBetaRequest(
  val assistant_id: String,
  val thread: ThreadRequest? = null,
  val model: String? = null,
  val instructions: String? = null,
  val tools: List<AssistantTool>? = null,
  val metadata: Map<String, String>? = null
)

