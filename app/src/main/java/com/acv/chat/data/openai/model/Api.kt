package com.acv.chat.data.openai.model;

import arrow.core.raise.Raise
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.openai.common.OpenAIClient
import com.acv.chat.domain.DomainError
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

context(OpenAIClient)
class ModelApi {

  context(Raise<DomainError>)
  suspend fun models(): List<Model> = catch(onError = DomainError::UnknownDomainError) {
    val response = client.get("models")
    response.body()
  }

  context(Raise<DomainError>)
  suspend fun model(id: String): Model = catch(onError = DomainError::UnknownDomainError) {
    val response = client.get("models/$id") {
      contentType(ContentType.Application.Json)
    }
    response.body()
  }
}