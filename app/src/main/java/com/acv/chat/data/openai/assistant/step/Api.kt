package com.acv.chat.data.openai.assistant.step

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.openai.assistant.beta
import com.acv.chat.domain.DomainError
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import com.acv.chat.data.openai.common.OpenAIClient
import com.acv.chat.data.openai.chat.Counter

context(Counter, OpenAIClient)
class RunStepApi {

  context(Raise<DomainError>)
  suspend fun get(
    threadId: String,
    runId: String,
    stepId: String
  ): RunStep =
    catch(
      onError = DomainError::UnknownDomainError
    ) {

      val response = client.get("threads/$threadId/runs/$runId/steps/$stepId") {
        headers {  beta()  }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }
}