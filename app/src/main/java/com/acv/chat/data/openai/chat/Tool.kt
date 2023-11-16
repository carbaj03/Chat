package com.acv.chat.data.openai.chat

import com.acv.chat.data.openai.Parameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tool(
  @SerialName("type") val type: ToolType,
  @SerialName("description") val description: String? = null,
  @SerialName("function") val function: FunctionTool,
) {

  companion object {
    fun function(name: String, description: String? = null, parameters: Parameters): Tool =
      Tool(type = ToolType.Function, description = description, function = FunctionTool(name = name, parameters = parameters))
  }
}

@Serializable
data class FunctionTool(
  @SerialName("name") val name: String,
  @SerialName("parameters") val parameters: Parameters
)
