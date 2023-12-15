package com.acv.chat.data.openai.assistant.runs

import arrow.core.raise.Raise
import com.acv.chat.data.openai.assistant.ActionSolver
import com.acv.chat.data.openai.assistant.AssistantTool
import com.acv.chat.data.openai.common.ModelId
import com.acv.chat.domain.DomainError
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Run(
  @SerialName("id") val runId: RunId,
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

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Run.Status::class)
object StatusSerializer : KSerializer<Run.Status> {
  override fun serialize(encoder: Encoder, value: Run.Status) {
    encoder.encodeString(value.value)
  }

  override fun deserialize(decoder: Decoder): Run.Status {
    val value = decoder.decodeString()
    return when (value) {
      "queued" -> Run.Status.Queued
      "in_progress" -> Run.Status.InProgress
      "requires_action" -> Run.Status.RequiresAction
      "cancelled" -> Run.Status.Cancelled
      "cancelling" -> Run.Status.Cancelling
      "failed" -> Run.Status.Failed
      "completed" -> Run.Status.Completed
      "expired" -> Run.Status.Expired
      else -> throw IllegalArgumentException("Unknown Status value: $value")
    }
  }
}

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
  @SerialName("submit_tool_outputs") val submit_tool_outputs: ToolOutputs
)

@Serializable
data class ToolOutputs(
  @SerialName("tool_calls") val tool_calls: List<ToolCall>
)

@Serializable
data class ToolCall(
  @SerialName("id") val id: String,
  @SerialName("function") val function: FunctionCall
)

@Serializable
data class FunctionCall(
  @SerialName("name") val name: String,
  @SerialName("arguments") val arguments: String
)

context(Raise<DomainError>, ActionSolver)
suspend fun Run.solveActions(): List<ToolOutput> =
  requiredAction?.submit_tool_outputs?.tool_calls
    ?.let { solve(it) }
    ?: raise(DomainError.UnknownDomainError("No tool calls"))
