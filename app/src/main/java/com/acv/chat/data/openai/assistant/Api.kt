package com.acv.chat.data.openai.assistant

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.openai.ModelId
import com.acv.chat.data.openai.OpenAIClient
import com.acv.chat.data.openai.assistant.message.ModifyAssistantRequest
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

context(Counter, OpenAIClient)
class AssistantApi {

  context(Raise<DomainError>)
  suspend fun createAssistant(
    model: ModelId = ModelId.Gpt35,
    tools: List<Tool> = listOf(Tool("code_interpreter")),
  ): AssistantObjectBeta =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val request = CreateAssistantBetaRequest(
        model = model.id,
        name = "My Assistant",
        instructions = "This is my assistant",
        tools = tools,
      )

      val response = client.post("assistants") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<AssistantObjectBeta>()
    }

  context(Raise<DomainError>)
  suspend fun get(
    id: AssistantId,
  ): AssistantObjectBeta =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val response = client.post("assistants/$id") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<AssistantObjectBeta>()
    }

  context(Raise<DomainError>)
  suspend fun modify(
    id: AssistantId,
    model: ModelId = ModelId.Gpt35,
  ): AssistantObjectBeta =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val request = ModifyAssistantRequest(
        model = model.id,
        name = "My Assistant",
      )

      val response = client.post("assistants/$id") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
        setBody(request)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<AssistantObjectBeta>()
    }

  context(Raise<DomainError>)
  suspend fun delete(
    id: AssistantId,
  ): DeletedAssistantResponse =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {

      val response = client.delete("assistants/$id") {
        headers { append("OpenAI-Beta", "assistants=v1") }
        contentType(ContentType.Application.Json)
      }

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }

      response.body<DeletedAssistantResponse>()
    }

  context(Raise<DomainError>)
  suspend fun assistants(): AssistantListResponse =
    catch(
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
    catch(
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