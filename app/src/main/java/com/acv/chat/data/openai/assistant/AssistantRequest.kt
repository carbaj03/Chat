package com.acv.chat.data.openai.assistant

import com.acv.chat.data.openai.Parameters
import kotlinx.serialization.Serializable

@Serializable
data class CreateAssistantBetaRequest(
  val model: String,
  val name: String? = null,
  val description: String? = null,
  val instructions: String? = null,
  val tools: List<Tool> = emptyList(),
  val file_ids: List<String> = emptyList(),
  val metadata: Map<String, String>? = null
)

@Serializable
data class Tool(
  val type: String,
  val function: Function? = null
) {
  companion object {
    val CodeInterpreter = Tool(type = "code_interpreter")
    val Retrieval = Tool(type = "retrieval")
    fun function(
      name: String,
      description: String,
      parameters: Parameters
    ): Tool =
      Tool(
        type = "function",
        function = Function(
          name = name,
          description = description,
          parameters = parameters
        )
      )
  }
}

//@Serializable
//sealed interface Tool {
//  val type: String
//}
//
//@Serializable
//data class CodeInterpreterTool(
//  override val type: String = "code_interpreter"
//) : Tool
//
//@Serializable
//data class RetrievalTool(
//  override val type: String = "retrieval"
//) : Tool
//
//@Serializable
//data class FunctionTool(
//  override val type: String = "function",
//  val function: Function
//) : Tool

@Serializable
data class Function(
  val description: String? = null,
  val name: String,
  val parameters: Parameters,
  val file_ids: List<String> = emptyList()
)

//@Serializable
//data class Parameters(
//  val type: String = "object",
//  val properties: JsonObject? = null
//)

@Serializable
data class JsonSchemaObject(
  val type: String,
  val format: String? = null,
  val items: JsonSchemaObject? = null,
  val properties: Map<String, JsonSchemaObject>? = null,
  val required: List<String>? = null,
  val additionalProperties: Boolean? = null
)

@Serializable
data class AssistantObjectBeta(
  val id: AssistantId,
  val `object`: String = "assistant",
  val created_at: Int,
  val name: String? = null,
  val description: String? = null,
  val model: String,
  val instructions: String? = null,
  val tools: List<Tool>,
  val file_ids: List<String>,
  val metadata: Map<String, String>?
)

@Serializable
@JvmInline
value class AssistantId(val value: String)