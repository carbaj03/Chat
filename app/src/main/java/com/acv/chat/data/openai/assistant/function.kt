package com.acv.chat.data.openai.assistant

import arrow.core.raise.Raise
import com.acv.chat.arrow.error.onError
import com.acv.chat.data.openai.Parameters
import com.acv.chat.data.openai.assistant.runs.Action
import com.acv.chat.data.openai.assistant.runs.AssistantTool
import com.acv.chat.data.openai.assistant.runs.Function
import com.acv.chat.data.openai.assistant.runs.ToolOutput
import com.acv.chat.data.schema.buildJsonSchema
import com.acv.chat.domain.DomainError
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified A> function(
  serializer: KSerializer<A>,
  name: String? = null,
  description: String? = null,
): AssistantTool {
  val descriptor = serializer.descriptor
  val parameters = buildJsonSchema(descriptor)
  val fnName = descriptor.serialName.substringAfterLast(".").lowercase()

  return AssistantTool(
    type = "function",
    function = Function(
      name = name ?: fnName,
      description = description ?: "Generated function for $fnName",
      parameters = Parameters(parameters)
    )
  )
}

context(Raise<DomainError>)
inline fun <reified A> functionWithAction(
  name: String? = null,
  description: String? = null,
  crossinline block: suspend (A) -> String?
): FunctionWithAction =
  onError(
    onError = { DomainError.UnknownDomainError(it) }
  ) {
    val serializer = serializer<A>()
    val tool = function<A>(serializer, name, description)
    val action: Action = { id, args ->
      val action = block(Json.decodeFromString(serializer, args)) ?: throw Exception("Error decoding arguments")
      ToolOutput(id, action)
    }
    return FunctionWithAction(tool, action)
  }

data class FunctionWithAction(
  val tool: AssistantTool,
  val action: Action,
)