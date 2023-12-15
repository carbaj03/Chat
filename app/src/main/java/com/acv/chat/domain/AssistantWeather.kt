package com.acv.chat.domain

import arrow.core.raise.Raise
import com.acv.chat.arrow.orNull
import com.acv.chat.data.FileSource
import com.acv.chat.data.openai.assistant.Assistant
import com.acv.chat.data.openai.assistant.AssistantApi
import com.acv.chat.data.openai.assistant.actionSolver
import com.acv.chat.data.openai.assistant.add
import com.acv.chat.data.openai.assistant.addCodeInterpreter
import com.acv.chat.data.openai.assistant.addRetrievalTool
import com.acv.chat.data.openai.assistant.file.Purpose
import com.acv.chat.data.openai.assistant.message.ThreadMessageApi
import com.acv.chat.data.openai.assistant.message.firstMessage
import com.acv.chat.data.openai.assistant.runs.AssistantId
import com.acv.chat.data.openai.assistant.runs.RunApi
import com.acv.chat.data.openai.assistant.runs.ThreadId
import com.acv.chat.data.openai.assistant.runs.invoke
import com.acv.chat.data.openai.assistant.thread.ThreadApi
import com.acv.chat.data.openai.common.ModelId
import com.acv.chat.data.openai.file.FileUpload
import com.acv.chat.data.openai.file.FilesApi
import com.acv.chat.data.weather.WeatherInfo
import com.acv.chat.data.weather.WeatherRepository
import okio.Source

interface AssistantWeather {

  context(Raise<DomainError>)
  suspend fun getWeather(city: String): String

  context(Raise<DomainError>)
  suspend fun updateAssistant(source: Source): Assistant
}

context(WeatherRepository, AssistantApi, ThreadApi, RunApi, ThreadMessageApi, FilesApi)
class AssistantWeatherImpl : AssistantWeather {

  private val assistantId = AssistantId("asst_HGNu00qRQiwCJwXzhRGyd4k4")
  private val threadId: ThreadId? = null

  private val actionSolver = actionSolver {
    add<WeatherInfo> { weather ->
      orNull { getWeather(weather.latitude, weather.longitude) }
    }
    addCodeInterpreter()
    addRetrievalTool()
  }

  context(Raise<DomainError>)
  override suspend fun getWeather(city: String): String = with(actionSolver) {
//    val file = fileApi.file(FileUpload(file = FileSource(source = "asdfasdf".asSource(), name = "asdfasdf"), purpose = Purpose.assistants))
//    val assistant = either {
//      get(assistantId)
//    }.getOrElse {
//      createAssistant(model = ModelId.Gpt4Preview)
//    }
//    assistant = modify(assistant.id, model = ModelId.Gpt4Preview, files = listOf(file.id))
    val id = threadId ?: createThread().id
    createMessage(prompt = "The weather in $city", threadId = id)
    createRun(assistantId = assistantId, threadId = id)()

    return all(id).firstMessage ?: raise(DomainError.UnknownDomainError("No result"))
  }

  context(Raise<DomainError>)
  override suspend fun updateAssistant(
    source: Source
  ): Assistant = with(actionSolver) {
    val fileToUpload = FileUpload(
      file = FileSource(name = "mydoc", source = source),
      purpose = Purpose.assistants
    )
    val file = upload(fileToUpload)
    return modify(
      assistantId = assistants().data.first().id,
      model = ModelId.Gpt4Preview,
      files = listOf(file.id)
    )
  }
}