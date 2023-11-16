package com.acv.chat.data.openai.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = ToolChoiceSerializer::class)
sealed interface ToolChoice {
    @JvmInline
    value class Mode(val value: String) : ToolChoice

    @Serializable
    data class Named(
        @SerialName("type") val type: ToolType? = null,
        @SerialName("function") val function: FunctionToolChoice? = null,
    ) : ToolChoice

    companion object {
        val Auto: ToolChoice = Mode("auto")
        val None: ToolChoice = Mode("none")
        fun function(name: String): ToolChoice =
            Named(type = ToolType.Function, function = FunctionToolChoice(name = name))
    }
}

@Serializable
data class FunctionToolChoice(val name: String)