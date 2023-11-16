package com.acv.chat.data.openai.assistant.step

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.catch
import com.acv.chat.domain.DomainError
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

context(com.acv.chat.data.openai.chat.Counter, com.acv.chat.data.openai.OpenAIClient)
class RunStepApi {

  context(Raise<DomainError>)
  suspend fun get(
    threadId: String,
    runId: String,
    stepId: String
  ): RunStepObjectBeta =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val response = client.get("threads/$threadId/runs/$runId/steps/$stepId") {
        headers {  append("OpenAI-Beta", "assistants=v1")  }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<RunStepObjectBeta>()
    }
}