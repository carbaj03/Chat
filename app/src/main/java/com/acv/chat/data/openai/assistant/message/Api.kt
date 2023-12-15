package com.acv.chat.data.openai.assistant.message

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.openai.assistant.MessageId
import com.acv.chat.data.openai.assistant.beta
import com.acv.chat.data.openai.assistant.get
import com.acv.chat.data.openai.assistant.post
import com.acv.chat.data.openai.assistant.runs.ThreadId
import com.acv.chat.data.openai.assistant.thread.Thread
import com.acv.chat.data.openai.chat.ChatRole
import com.acv.chat.domain.DomainError
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import com.acv.chat.data.openai.common.OpenAIClient
import com.acv.chat.data.openai.chat.Counter

context(Counter, OpenAIClient)
class ThreadMessageApi {

  context(Raise<DomainError>)
  suspend fun createMessage(
    prompt: String,
    threadId: ThreadId
  ): Message =
    post(
      url = "threads/${threadId.id}/messages",
      request = CreateMessageRequest(
        role = ChatRole.User.role,
        content = prompt,
        metadata = null,
      )
    )

  context(Raise<DomainError>)
  suspend fun get(
    threadId: ThreadId,
    messageId: MessageId
  ): Message =
    get(url = "threads/${threadId.id}/messages/${messageId.id}")

  context(Raise<DomainError>)
  suspend fun all(
    threadId: ThreadId,
    queryParameters: QueryParameters = QueryParameters()
  ): Messages =
    get(
      url = "threads/${threadId.id}/messages",
      queryParameters = queryParameters
    )
}

data class QueryParameters(
  val limit: Int = 20,
  val order: String = "desc",
  val after: String? = null,
  val before: String? = null
)

context(URLBuilder)
fun QueryParameters.addParameters() {
  parameters.append("limit", limit.toString())
  parameters.append("order", order)
  after?.let { parameters.append("after", it) }
  before?.let { parameters.append("before", it) }
}

context(Raise<DomainError>, ThreadMessageApi)
suspend fun Thread.prompt(message: String) {
  createMessage(message, id)
}