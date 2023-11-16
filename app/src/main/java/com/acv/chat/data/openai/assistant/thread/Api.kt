package com.acv.chat.data.openai.assistant.thread

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.catch
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
class ThreadApi {

  context(Raise<DomainError>)
  suspend fun createThread(): ThreadResponse =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val request = ThreadCreateRequest()

      val response = client.post("threads") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<ThreadResponse>()
    }

  context(Raise<DomainError>)
  suspend fun get(
    id: String
  ): ThreadResponse =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val response = client.get("threads/$id") {
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<ThreadResponse>()
    }
}