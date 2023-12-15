package com.acv.chat.data.openai.assistant

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.openai.assistant.message.QueryParameters
import com.acv.chat.data.openai.assistant.message.addParameters
import com.acv.chat.data.openai.common.OpenAIClient
import com.acv.chat.domain.DomainError
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

context(Raise<DomainError>, OpenAIClient)
suspend inline fun <reified A, reified B> post(
  url: String,
  request: B,
): A =
  catch(
    onError = DomainError::UnknownDomainError
  ) {
    val response = client.post(url) {
      setBody(request)
      headers { beta() }
      contentType(ContentType.Application.Json)
    }

    ensure(response.status.isSuccess()) {
      raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
    }

    response.body<A>()
  }

context(Raise<DomainError>, OpenAIClient)
suspend inline fun <reified A> get(
  url: String,
  queryParameters: QueryParameters? = null,
): A =
  catch(
    onError = DomainError::UnknownDomainError
  ) {

    val response = client.get(url) {
      headers { beta() }
      contentType(ContentType.Application.Json)
      queryParameters?.let { url { queryParameters.addParameters() } }
    }

    ensure(response.status.isSuccess()) {
      raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
    }

    response.body<A>()
  }

context(Raise<DomainError>, OpenAIClient)
suspend inline fun <reified A> delete(
  url: String,
): A =
  catch(
    onError = DomainError::UnknownDomainError
  ) {

    val response = client.delete(url) {
      headers { beta() }
      contentType(ContentType.Application.Json)
    }

    ensure(response.status.isSuccess()) {
      raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
    }

    response.body<A>()
  }