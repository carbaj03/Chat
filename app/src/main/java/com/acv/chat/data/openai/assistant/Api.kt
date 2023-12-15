package com.acv.chat.data.openai.assistant

import arrow.core.raise.Raise
import com.acv.chat.data.openai.assistant.file.FileId
import com.acv.chat.data.openai.assistant.runs.AssistantId
import com.acv.chat.data.openai.chat.Counter
import com.acv.chat.data.openai.common.ModelId
import com.acv.chat.data.openai.common.OpenAIClient
import com.acv.chat.domain.DomainError

context(Counter, OpenAIClient)
class AssistantApi {

  context(Raise<DomainError>, ActionSolver)
  suspend fun createAssistant(
    model: ModelId,
    files: List<FileId>? = null,
  ): Assistant =
    post(
      url = "assistants",
      request = AssistantRequest(
        model = model.id,
        name = "My Assistant",
        instructions = "This is my assistant",
        tools = tools.tools,
        fileIds = files,
      )
    )

  context(Raise<DomainError>)
  suspend fun get(
    assistantId: AssistantId,
  ): Assistant =
    get(url = "assistants/${assistantId.id}")

  context(Raise<DomainError>, ActionSolver)
  suspend fun modify(
    assistantId: AssistantId,
    model: ModelId,
    files: List<FileId>? = null,
  ): Assistant =
    post(
      url = "assistants/${assistantId.id}",
      request = AssistantRequest(
        model = model.id,
        name = "My Assistant",
        instructions = "This is my assistant v2",
        tools = tools.tools,
        fileIds = files,
      )
    )

  context(Raise<DomainError>)
  suspend fun delete(
    id: AssistantId,
  ): DeletedAssistantResponse =
    delete(url = "assistants/$id")

  context(Raise<DomainError>)
  suspend fun assistants(): Assistants =
    get(url = "assistants")

}