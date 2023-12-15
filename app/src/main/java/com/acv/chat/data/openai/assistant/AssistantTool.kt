package com.acv.chat.data.openai.assistant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class AssistantTool(
//  @SerialName("type") val type: String,
//  @SerialName("function") val function: Function? = null,
//)
//
//val codeInterpreter = AssistantTool("code_interpreter")
//val retrieval = AssistantTool("retrieval")

@Serializable
sealed interface AssistantTool {

  @Serializable
  @SerialName("code_interpreter")
  data object CodeInterpreter : AssistantTool

  @Serializable
  @SerialName("retrieval")
  data object RetrievalTool : AssistantTool

  @Serializable
  @SerialName("function")
  data class FunctionTool(
    @SerialName("function") val function: Function,
  ) : AssistantTool
}
