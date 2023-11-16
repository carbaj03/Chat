package com.acv.chat.data.openai.chat

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
  @SerialName("role") val role: ChatRole,
  @SerialName("content") val messageContent: Content? = null,
  @SerialName("name") val name: String? = null,
  @SerialName("tool_calls") val toolCalls: List<ToolCall>? = null,
  @SerialName("tool_call_id") val toolCallId: ToolId? = null,
) {

  constructor(
    role: ChatRole,
    content: String? = null,
    name: String? = null,
    toolCalls: List<ToolCall>? = null,
    toolCallId: ToolId? = null,
  ) : this(
    role = role,
    messageContent = content?.let { TextContent(it) },
    name = name,
    toolCalls = toolCalls,
    toolCallId = toolCallId,
  )

  constructor(
    role: ChatRole,
    content: List<ContentPart>? = null,
    name: String? = null,
    toolCalls: List<ToolCall>? = null,
    toolCallId: ToolId? = null,
  ) : this(
    role = role,
    messageContent = content?.let { ListContent(it) },
    name = name,
    toolCalls = toolCalls,
    toolCallId = toolCallId,
  )

  val content: String?
    get() = when (messageContent) {
      is TextContent? -> messageContent?.content
      is ListContent? -> messageContent?.content?.joinToString("") { it.toString() }
      else -> ""
    }

  @Suppress("FunctionName")
  companion object {

    fun System(content: String? = null, name: String? = null): ChatMessage {
      return ChatMessage(
        role = ChatRole.System,
        messageContent = content?.let { TextContent(it) },
        name = name,
      )
    }

    fun User(content: String, name: String? = null): ChatMessage {
      return ChatMessage(
        role = ChatRole.User,
        messageContent = TextContent(content),
        name = name,
      )
    }

    fun User(content: List<ContentPart>, name: String? = null): ChatMessage {
      return ChatMessage(
        role = ChatRole.User,
        messageContent = ListContent(content),
        name = name,
      )
    }

    fun Assistant(
      content: String? = null,
      name: String? = null,
      toolCalls: List<ToolCall>? = null
    ): ChatMessage {
      return ChatMessage(
        role = ChatRole.Assistant,
        messageContent = content?.let { TextContent(it) },
        name = name,
        toolCalls = toolCalls,
      )
    }

    fun Tool(content: String? = null, toolCallId: ToolId): ChatMessage {
      return ChatMessage(
        role = ChatRole.Tool,
        messageContent = content?.let { TextContent(it) },
        toolCallId = toolCallId,
      )
    }
  }
}

@Serializable(with = ContentSerializer::class)
sealed interface Content

@JvmInline
@Serializable
value class TextContent(val content: String) : Content

@Serializable(with = ContentPartsSerializer::class)
data class ListContent(val content: List<ContentPart>) : Content

@Serializable(with = ContentPartSerializer::class)
sealed interface ContentPart {
  val type: String
}

@Serializable
data class TextPart(@SerialName("text") val text: String) : ContentPart {
  @SerialName("type")
  @Required
  override val type: String = "text"
}

@Serializable
data class ImagePart(
  @SerialName("image_url") val imageUrl: ImageURL,
) : ContentPart {
  @SerialName("type")
  @Required
  override val type: String = "image"

  constructor(url: String, detail: String? = null) : this(ImageURL(url = url, detail = detail))

  @Serializable
  data class ImageURL(
    @SerialName("url") val url: String,
    @SerialName("detail") val detail: String? = null,
  )
}