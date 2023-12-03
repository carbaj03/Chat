package com.acv.chat.data.openai.assistant.runs

import com.acv.chat.data.openai.ModelId
import com.acv.chat.data.openai.Parameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Run(
  @SerialName("id") val id: RunId,
  @SerialName("created_at") val createdAt: Int,
  @SerialName("thread_id") val threadId: ThreadId,
  @SerialName("assistant_id") val assistantId: AssistantId,
  @SerialName("status") val status: Status,
  @SerialName("required_action") val requiredAction: RequiredAction? = null,
  @SerialName("last_error") val lastError: LastError? = null,
  @SerialName("expires_at") val expiresAt: Int? = null,
  @SerialName("started_at") val startedAt: Int? = null,
  @SerialName("canceled_at") val canceledAt: Int? = null,
  @SerialName("failed_at") val failedAt: Int? = null,
  @SerialName("completed_at") val completedAt: Int? = null,
  @SerialName("model") val model: ModelId,
  @SerialName("instructions") val instructions: String? = null,
  @SerialName("tools") val tools: List<AssistantTool>? = null,
  @SerialName("file_ids") val fileIds: List<String>? = null,
  @SerialName("metadata") val metadata: Map<String, String>? = null,
) {

  @Serializable(with = StatusSerializer::class)
  sealed class Status(val value: String) {
    data object Queued : Status("queued")
    data object InProgress : Status("in_progress")
    data object RequiresAction : Status("requires_action")
    data object Cancelled : Status("cancelled")
    data object Cancelling : Status("cancelling")
    data object Failed : Status("failed")
    data object Completed : Status("completed")
    data object Expired : Status("expired")
  }
}

@Serializable
data class AssistantTool(
  @SerialName("type") val type: String ,
  @SerialName("function") val function: Function? = null,
)

val codeInterpreter = AssistantTool("code_interpreter")
val retrieval = AssistantTool("retrieval")

//@Serializable(with = AssistantToolSerializer::class)
//sealed class AssistantTool() {
//
//  @Serializable
//  data class CodeInterpreter(
//    @SerialName("type") val type: String = "code_interpreter",
//  ) : AssistantTool()
//
//  @Serializable
//  data class RetrievalTool(
//    @SerialName("type") val type: String = "retrieval",
//  ) : AssistantTool()
//
//  @Serializable
//  class FunctionTool(
//    @SerialName("type") val type: String = "function",
//    @SerialName("function") val function: Function,
//  ) : AssistantTool()
//}

@Serializable
data class Function(
  @SerialName("name") val name: String,
  @SerialName("description") val description: String,
  @SerialName("parameters") val parameters: Parameters,
)

@Serializable
@JvmInline
value class RunId(val id: String)

@Serializable
@JvmInline
value class ThreadId(val id: String)

@Serializable
@JvmInline
value class AssistantId(val id: String)

@Serializable
data class RequiredAction(
  val type: String = "submit_tool_outputs",
  val submit_tool_outputs: SubmitToolOutputs
)

@Serializable
data class SubmitToolOutputs(
  val tool_calls: List<ToolCall>
)

@Serializable
data class ToolCall(
  val id: String,
  val type: String = "function",
  val function: FunctionCall
)

@Serializable
data class FunctionCall(
  val name: String,
  val arguments: String
)

@Serializable
data class LastError(
  val code: String,
  val message: String
)