package com.acv.chat.data.openai.assistant.runs

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
class RunApi {

  context(Raise<DomainError>)
  suspend fun createAndRun(
    assistantId : String,
    thread: ThreadObject
  ): RunObjectBeta =
    catch(
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

      response.body<RunObjectBeta>()
    }

  context(Raise<DomainError>)
  suspend fun createRun(
    threadId: String,
    assistantId : String,
  ): RunObjectBeta =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val request = CreateRunBetaRequest(
        assistantId
      )

      val response = client.post("threads/$threadId/runs") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<RunObjectBeta>()
    }

  context(Raise<DomainError>)
  suspend fun getRun(
    threadId: String,
    runId: String,
  ): RunObjectBeta =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val response = client.get("threads/$threadId/runs/$runId") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<RunObjectBeta>()
    }


  context(Raise<DomainError>)
  suspend fun tools(
    threadId: String,
    runId: String,
  ): RunObjectBeta =
    catch(
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

      response.body<RunObjectBeta>()
    }


  context(Raise<DomainError>)
  suspend fun cancel(
    threadId: String,
    runId: String,
  ): RunObjectBeta =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val response = client.get("threads/$threadId/runs/$runId/cancel") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<RunObjectBeta>()
    }

  context(Raise<DomainError>)
  suspend fun submit_tool_outputs(
    treadId: String,
    runId: String,
    outputs : List<ToolOutput>
  ): RunObjectBeta =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val request = ToolOutputsRequest(outputs)

      val response = client.post("threads/$treadId/runs/$runId/submit_tool_outputs") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<RunObjectBeta>()
    }

}