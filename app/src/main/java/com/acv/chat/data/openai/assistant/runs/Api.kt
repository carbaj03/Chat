package com.acv.chat.data.openai.assistant.runs

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.onError
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
class RunApi {

  context(Raise<DomainError>)
  suspend fun createAndRun(
    assistantId : String,
    thread: ThreadObject
  ): Run =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val request = CreateThreadAndRunBetaRequest(
        assistantId,
        thread
      )

      val response = client.post("threads/runs") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun createRun(
    assistantId : AssistantId,
    threadId: ThreadId,
  ): Run =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val request = RunRequest(
       assistantId =  assistantId
      )

      val response = client.post("threads/${threadId.id}/runs") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun getRun(
    threadId: ThreadId,
    runId: RunId,
  ): Run =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val response = client.get("threads/${threadId.id}/runs/${runId.id}") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<Run>()
    }


  context(Raise<DomainError>)
  suspend fun tools(
    threadId: String,
    runId: String,
  ): Run =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val request = ToolOutputsRequest(listOf())

      val response = client.get("threads/$threadId/runs/$runId/submit_tool_outputs") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }


  context(Raise<DomainError>)
  suspend fun cancel(
    threadId: ThreadId,
    runId: RunId,
  ): Run =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val response = client.get("threads/${threadId.id}/runs/${runId.id}/cancel") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun submitToolOutputs(
    treadId: ThreadId,
    runId: RunId,
    outputs : List<ToolOutput>
  ): Run =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val request = ToolOutputsRequest(outputs)

      val response = client.post("threads/${treadId.id}/runs/${runId.id}/submit_tool_outputs") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<Run>()
    }

}