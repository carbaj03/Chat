package com.acv.chat.data.openai.assistant.step

import com.acv.chat.data.openai.assistant.MessageId
import com.acv.chat.data.openai.assistant.file.FileId
import com.acv.chat.data.openai.assistant.runs.AssistantId
import com.acv.chat.data.openai.assistant.runs.LastError
import com.acv.chat.data.openai.assistant.runs.Run
import com.acv.chat.data.openai.assistant.runs.RunId
import com.acv.chat.data.openai.assistant.runs.ThreadId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface RunStep {
  val id: RunStepId
  val createdAt: Int
  val assistantId: AssistantId
  val threadId: ThreadId
  val runId: RunId
  val status: Run.Status
  val stepDetails: RunStepDetails
  val lastError: LastError?
  val expiredAt: Int?
  val cancelledAt: Int?
  val failedAt: Int?
  val completedAt: Int?
  val metadata: Map<String, String>?
}

@Serializable
@JvmInline
value class RunStepId(val id: String)

@Serializable
sealed class StepDetails {
  abstract val type: String
}

@Serializable
sealed interface RunStepDetails

@Serializable
@SerialName("message_creation")
data class MessageCreationStepDetails(
  @SerialName("message_creation") val messageCreation: MessageCreation,
) : RunStepDetails

@Serializable
data class MessageCreation(
  @SerialName("message_id") val messageId: MessageId,
)

@Serializable
@SerialName("tool_calls")
data class ToolCallStepDetails(
  @SerialName("tool_calls") val toolCalls: List<ToolCallStep>? = null,
) : RunStepDetails

@Serializable
sealed interface ToolCallStep {

  @Serializable
  @SerialName("code_interpreter")
  data class CodeInterpreter(
    @SerialName("id") val id: ToolCallStepId,
    @SerialName("code_interpreter") val codeInterpreter: CodeInterpreterToolCall,
  ) : ToolCallStep

  @Serializable
  @SerialName("retrieval")
  data class RetrievalTool(
    @SerialName("id") val id: ToolCallStepId,
    @SerialName("retrieval") val retrieval: Map<String, String>,
  ) : ToolCallStep

  @Serializable
  @SerialName("function")
  data class FunctionTool(
    @SerialName("id") val id: ToolCallStepId,
    @SerialName("function") val function: FunctionToolCallStep,
  ) : ToolCallStep
}

@Serializable
data class FunctionToolCallStep(
  @SerialName("name") val name: String,
  @SerialName("arguments") val arguments: String,
  @SerialName("output") val output: String? = null,
)

@JvmInline
@Serializable
value class ToolCallStepId(val id: String)

@Serializable
data class CodeInterpreterToolCall(
  val input: String,
  val outputs: List<CodeInterpreterToolCallOutput>
)

@Serializable
sealed interface CodeInterpreterToolCallOutput {
  @Serializable
  @SerialName("logs")
  data class Logs(
    @SerialName("text") val text: String? = null,
  ) : CodeInterpreterToolCallOutput

  @Serializable
  @SerialName("image")
  data class Image(
    @SerialName("image") val image: CodeInterpreterImage,
  ) : CodeInterpreterToolCallOutput
}

@Serializable
data class CodeInterpreterImage(
  @SerialName("file_id") val fileId: FileId,
)
