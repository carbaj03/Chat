package com.acv.chat.data.openai.assistant.step

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class RunStepObjectBeta(
  val id: String,
  val `object`: String = "thread.run.step",
  val created_at: Int,
  val assistant_id: String,
  val thread_id: String,
  val run_id: String,
  val type: String,
  val status: String,
  val step_details: StepDetails,
  val last_error: LastError? = null,
  val expired_at: Int? = null,
  val cancelled_at: Int? = null,
  val failed_at: Int? = null,
  val completed_at: Int? = null,
  val metadata: Map<String, String>? = null
)

@Serializable
sealed class StepDetails {
  abstract val type: String
}

@Serializable
data class MessageCreationStepDetails(
  override val type: String = "message_creation",
  val message_creation: MessageCreationDetails
) : StepDetails()

@Serializable
data class MessageCreationDetails(
  val message_id: String
)

@Serializable
data class ToolCallsStepDetails(
  override val type: String = "tool_calls",
  val tool_calls: List<ToolCallDetails>
) : StepDetails()

@Serializable
sealed class ToolCallDetails {
  abstract val id: String
  abstract val type: String
}

@Serializable
data class CodeInterpreterToolCallDetails(
  override val id: String,
  override val type: String = "code_interpreter",
  val code_interpreter: CodeInterpreterDetails
) : ToolCallDetails()

@Serializable
data class CodeInterpreterDetails(
  val input: String,
  val outputs: List<JsonElement> // Assuming outputs can be of various types, represented as JSON elements
)

@Serializable
data class RetrievalToolCallDetails(
  override val id: String,
  override val type: String = "retrieval",
  val retrieval: JsonElement // Assuming retrieval details are represented as a JSON element
) : ToolCallDetails()

@Serializable
data class FunctionToolCallDetails(
  override val id: String,
  override val type: String = "function",
  val function: FunctionDetails
) : ToolCallDetails()

@Serializable
data class FunctionDetails(
  val name: String,
  val arguments: String,
  val output: String? = null
)

@Serializable
data class LastError(
  val code: String,
  val message: String
)