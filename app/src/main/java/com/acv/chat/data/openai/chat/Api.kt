package com.acv.chat.data.openai.chat

import android.util.Base64
import arrow.core.NonEmptyList
import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.core.toNonEmptyListOrNull
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.FileSource
import com.acv.chat.data.openai.chat.ChatMessage.Companion.User
import com.acv.chat.data.openai.chat.chunk.ChatCompletionChunk
import com.acv.chat.data.openai.common.ModelId.Gpt4
import com.acv.chat.data.openai.common.ModelId.Gpt4Vision
import com.acv.chat.data.openai.common.OpenAIClient
import com.acv.chat.domain.DomainError
import com.acv.chat.domain.DomainError.NetworkDomainError
import com.acv.chat.domain.DomainError.UnknownDomainError
import com.acv.chat.domain.Media
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.append
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import okio.buffer
import java.io.File

interface ChatService {

  context(Raise<DomainError>)
  suspend fun chat(
    prompt: String,
    files: NonEmptyList<Media>? = null,
  ): String

  context(Raise<DomainError>)
  fun chatCompletions(
    prompt: String,
    files: NonEmptyList<File>?,
  ): Flow<ChatCompletionChunk>
}

class ChatServiceMock : ChatService {

  context(Raise<DomainError>)
  override suspend fun chat(
    prompt: String,
    files: NonEmptyList<Media>?,
  ): String =
    "Prompt $prompt"

  context(Raise<DomainError>)
  override fun chatCompletions(
    prompt: String,
    files: NonEmptyList<File>?,
  ): Flow<ChatCompletionChunk> =
    flow { }
}

context(Counter, OpenAIClient)
class ChatApi : ChatService {
  private var context: MutableList<ChatMessage> = mutableListOf()

  context(Raise<DomainError>)
  override suspend fun chat(
    prompt: String,
    files: NonEmptyList<Media>?,
  ): String =
    catch(
      onError = ::UnknownDomainError
    ) {
      val model = files?.let { Gpt4Vision } ?: Gpt4

      val prompts = buildList {
        add(TextPart(prompt))
        files?.toNonEmptyListOrNull()?.let {
          it.forEach {
            when (it) {
              is Media.Image -> {
                val img = Base64.encodeToString(File(it.file).readBytes(), Base64.DEFAULT)
                add(ImagePart("data:image/jpeg;base64,$img"))
              }
              is Media.Pdf -> {

              }
            }
          }
        }
      }

      val request = ChatCompletionRequest(
        model = model.id,
        messages = context + User(prompts),
      )

      countTokens(model, request.messages)

      val response = client.post("chat/completions") {
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      context.add(User(prompts))

      ensure(response.status.isSuccess()) {
        raise(NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<ChatCompletion>().choices.firstOrNull()?.message?.let { assistant ->
        countTokens(model, assistant)
        context.add(assistant)
        assistant.content
      } ?: throw Exception("No content")
    }

  context(Raise<DomainError>)
  override fun chatCompletions(
    prompt: String,
    files: NonEmptyList<File>?,
  ): Flow<ChatCompletionChunk> {
    val model = files?.let { Gpt4Vision } ?: Gpt4
    val request = ChatCompletionRequest(
      model = model.id,
      messages = context + User(prompt),
    )

    return flow {
      val response = client.post("chat/completions") {
        setBody(streamRequestOf(request))
        contentType(ContentType.Application.Json)
        accept(ContentType.Text.EventStream)
        headers {
          append(HttpHeaders.CacheControl, "no-cache")
          append(HttpHeaders.Connection, "keep-alive")
        }
      }
      streamEventsFrom(response)
    }
  }
}

internal val JsonLenient = Json {
  isLenient = true
  ignoreUnknownKeys = true
}

internal inline fun <reified T> streamRequestOf(serializable: T): JsonElement {
  val enableStream = "stream" to JsonPrimitive(true)
  val json = JsonLenient.encodeToJsonElement(serializable)
  val map = json.jsonObject.toMutableMap().also { it += enableStream }
  return JsonObject(map)
}

internal fun FormBuilder.appendFileSource(key: String, fileSource: FileSource) {
  append(key = key, filename = fileSource.name, contentType = ContentType.Application.OctetStream) {
    fileSource.source.buffer().use { source ->
      val buffer = ByteArray(8192) // 8 KiB
      var bytesRead: Int
      while (source.read(buffer).also { bytesRead = it } != -1) {
        writeFully(src = buffer, offset = 0, length = bytesRead)
      }
    }
  }
}

private const val STREAM_PREFIX = "data:"
private const val STREAM_END_TOKEN = "$STREAM_PREFIX [DONE]"

internal suspend inline fun <reified T> FlowCollector<T>.streamEventsFrom(response: HttpResponse) {
  val channel: ByteReadChannel = response.body<ByteReadChannel>()
  while (!channel.isClosedForRead) {
    val line = channel.readUTF8Line() ?: continue
    val value: T = when {
      line.startsWith(STREAM_END_TOKEN) -> break
      line.startsWith(STREAM_PREFIX) -> JsonLenient.decodeFromString(line.removePrefix(STREAM_PREFIX))
      else -> continue
    }
    emit(value)
  }
}