package com.acv.chat.data.openai.chat;

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ChatCompletionRequest(
  @SerialName("model") val model: String,
  @SerialName("messages") val messages: List<ChatMessage>,
  @SerialName("temperature") val temperature: Double? = null,
  @SerialName("top_p") val topP: Double? = null,
  @SerialName("n") val n: Int? = null,
  @SerialName("stop") val stop: List<String>? = null,
  @SerialName("max_tokens") val maxTokens: Int? = null,
  @SerialName("presence_penalty") val presencePenalty: Double? = null,
  @SerialName("frequency_penalty") val frequencyPenalty: Double? = null,
  @SerialName("logit_bias") val logitBias: Map<String, Int>? = null,
  @SerialName("user") val user: String? = null,
  @SerialName("response_format") val responseFormat: ChatResponseFormat? = null,
  @SerialName("tools") val tools: List<Tool>? = null,
  @SerialName("tool_choice") val toolChoice: ToolChoice? = null,
  @SerialName("seed") val seed: Int? = null,
)
