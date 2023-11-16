package com.acv.chat.data.openai.assistant.runs

import com.acv.chat.data.openai.assistant.Tool
import kotlinx.serialization.Serializable

@Serializable
data class RunObjectBeta(
    val id: String,
    val `object`: String = "thread.run",
    val created_at: Int,
    val thread_id: String,
    val assistant_id: String,
    val status: Status,
    val required_action: RequiredAction? = null,
    val last_error: LastError? = null,
    val expires_at: Int? = null,
    val started_at: Int? = null,
    val cancelled_at: Int? = null,
    val failed_at: Int? = null,
    val completed_at: Int? = null,
    val model: String,
    val instructions: String,
    val tools: List<Tool>,
    val file_ids: List<String>,
    val metadata: Map<String, String>? = null
)

@Serializable
enum class Status {
    queued, inProgress, completed, failed, cancelled, requiresAction
}

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