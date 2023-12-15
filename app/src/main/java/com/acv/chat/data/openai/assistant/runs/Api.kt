package com.acv.chat.data.openai.assistant.runs

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.openai.assistant.ActionSolver
import com.acv.chat.data.openai.assistant.AssistantApi
import com.acv.chat.data.openai.assistant.beta
import com.acv.chat.data.openai.assistant.message.ThreadMessageApi
import com.acv.chat.data.openai.assistant.thread.ThreadApi
import com.acv.chat.data.openai.assistant.thread.ThreadRequest
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

import com.acv.chat.data.openai.common.OpenAIClient
import com.acv.chat.data.openai.chat.Counter

context(Counter, OpenAIClient)
class RunApi {

  context(Raise<DomainError>)
  suspend fun createAndRun(
    assistantId: String,
    thread: ThreadRequest
  ): Run =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val request = CreateThreadAndRunBetaRequest(
        assistantId,
        thread
      )

      val response = client.post("threads/runs") {
        headers { beta() }
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
    assistantId: AssistantId,
    threadId: ThreadId,
  ): Run =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val request = RunRequest(
        assistantId = assistantId
      )

      val response = client.post("threads/${threadId.id}/runs") {
        headers { beta() }
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
    runId: RunId,
    threadId: ThreadId,
  ): Run =
    catch(
      onError = DomainError::UnknownDomainError
    ) {

      val response = client.get("threads/${threadId.id}/runs/${runId.id}") {
        headers { beta() }
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
    catch(
      onError = DomainError::UnknownDomainError
    ) {

      val request = ToolOutputsRequest(listOf())

      val response = client.get("threads/$threadId/runs/$runId/submit_tool_outputs") {
        headers { beta() }
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
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val response = client.get("threads/${threadId.id}/runs/${runId.id}/cancel") {
        headers { beta() }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun submitToolOutputs(
    runId: RunId,
    treadId: ThreadId,
    outputs: List<ToolOutput>
  ): Run =
    catch(
      onError = DomainError::UnknownDomainError
    ) {

      val request = ToolOutputsRequest(outputs)

      val response = client.post("threads/${treadId.id}/runs/${runId.id}/submit_tool_outputs") {
        headers { beta() }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<Run>()
    }
}

class AssistantOAI(
  val assistantId: AssistantId,
  val threadId: ThreadId,
)

context(Raise<DomainError>, RunApi, AssistantApi, ThreadApi, ThreadMessageApi, ActionSolver)
suspend fun AssistantOAI.run() {
  createRun(assistantId, threadId)()
}
