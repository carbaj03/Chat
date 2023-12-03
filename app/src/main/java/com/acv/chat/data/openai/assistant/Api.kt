package com.acv.chat.data.openai.assistant

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.onError
import com.acv.chat.data.openai.ModelId
import com.acv.chat.data.openai.OpenAIClient
import com.acv.chat.data.openai.assistant.file.AssistantFile
import com.acv.chat.data.openai.assistant.file.FileId
import com.acv.chat.data.openai.assistant.runs.ActionSolver
import com.acv.chat.data.openai.assistant.runs.AssistantId
import com.acv.chat.data.openai.assistant.runs.AssistantTool
import com.acv.chat.data.openai.chat.Counter
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

@JvmInline
value class Tools(val tools: List<AssistantTool>)

context(Counter, OpenAIClient)
class AssistantApi {

  context(Raise<DomainError>, ActionSolver)
  suspend fun createAssistant(
    model: ModelId ,
  ): Assistant =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val request = AssistantRequest(
        model = model.id,
        name = "My Assistant",
        instructions = "This is my assistant",
        tools = tools.tools,
        fileIds = listOf(),
      )

      val response = client.post("assistants") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<Assistant>()
    }

  context(Raise<DomainError>)
  suspend fun get(
    assistantId: AssistantId,
  ): Assistant =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val response = client.post("assistants/${assistantId.id}") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun modify(
    assistantId: AssistantId,
    model: ModelId ,
    files: List<FileId>? = null,
    tools: List<AssistantTool>? = null,
  ): Assistant =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val request = AssistantRequest(
        model = model.id,
        name = "My Assistant",
        instructions = "This is my assistant v2",
//        tools = tools,
        fileIds = files,
      )

      val response = client.post("assistants/${assistantId.id}") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun delete(
    id: AssistantId,
  ): DeletedAssistantResponse =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val response = client.delete("assistants/$id") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun assistants(): AssistantListResponse =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val response = client.get("assistants") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<AssistantListResponse>()
    }

  context(Raise<DomainError>)
  suspend fun file(
    id: AssistantId,
  ): AssistantFile =
    onError(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val response = client.post("assistants/$id/files") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<AssistantFile>()
    }
}