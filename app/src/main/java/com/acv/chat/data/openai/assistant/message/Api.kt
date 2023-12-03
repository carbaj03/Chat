package com.acv.chat.data.openai.assistant.message

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.onError
import com.acv.chat.data.openai.assistant.MessageId
import com.acv.chat.data.openai.assistant.runs.ThreadId
import com.acv.chat.data.openai.chat.ChatRole
import com.acv.chat.domain.DomainError
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

context(com.acv.chat.data.openai.chat.Counter, com.acv.chat.data.openai.OpenAIClient)
class ThreadMessageApi {

  context(Raise<DomainError>)
  suspend fun createMessage(
    prompt: String,
    threadId: ThreadId,
  ): MessageResponse =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val request = CreateMessageRequest(
        role = ChatRole.User.role,
        content = prompt,
        metadata = null,
      )

      val response = client.post("threads/${threadId.id}/messages") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<MessageResponse>()
    }

  context(Raise<DomainError>)
  suspend fun get(
    threadId: ThreadId,
    messageId: MessageId
  ): MessageResponse =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val response = client.get("threads/${threadId.id}/messages/${messageId.id}") {
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<MessageResponse>()
    }

  context(Raise<DomainError>)
  suspend fun all(
    threadId: ThreadId,
  ): MessageListResponse =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val response = client.get("threads/${threadId.id}/messages") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
        url {
//          parameters.append("limit", "0")
//          parameters.append("order", "abc123")
//          parameters.append("after", "abc123")
//          parameters.append("before", "abc123")
        }
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<MessageListResponse>()
    }
}