package com.acv.chat.data.openai.assistant.file

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.openai.common.OpenAIClient
import com.acv.chat.data.openai.assistant.SortOrder
import com.acv.chat.data.openai.assistant.beta
import com.acv.chat.data.openai.assistant.runs.AssistantId
import com.acv.chat.domain.DomainError
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

context(com.acv.chat.data.openai.chat.Counter, OpenAIClient)
class FileAssistantApi {

  context(Raise<DomainError>)
  suspend fun create(
    assistantId: AssistantId,
    fileId: FileId
  ): AssistantFile =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val request = buildJsonObject { put("file", fileId.id) }

      val response = client.post("assistants/${assistantId.id}") {
        setBody(request)
        contentType(ContentType.Application.Json)
        headers { beta() }
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }
  
  context(Raise<DomainError>)
  suspend fun delete(
    assistantId: AssistantId,
    fileId: FileId
  ): FileDeletedEvent =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val response = client.delete("assistants/${assistantId.id}/files/${fileId.id}") {
        contentType(ContentType.Application.Json)
        headers { beta() }
      }
      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun file(
    assistantId: AssistantId,
    fileId: FileId
  ): AssistantFile =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val response = client.get("assistants/${assistantId.id}/files/${fileId.id}") {
        contentType(ContentType.Application.Json)
        headers { beta() }
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun files(
    id: AssistantId,
    limit: Int?,
    order: SortOrder?,
    after: String?,
    before: String?
  ): List<AssistantFile> =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val response = client.get("assistants/${id.id}/files") {
        url {
          limit?.let { parameter("limit", it) }
          order?.let { parameter("order", it.order) }
          after?.let { parameter("after", it) }
          before?.let { parameter("before", it) }
        }
        contentType(ContentType.Application.Json)
        headers { beta() }
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }
}