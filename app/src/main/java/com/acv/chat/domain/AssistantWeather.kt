package com.acv.chat.domain

import arrow.core.raise.Raise
import com.acv.chat.arrow.orNull
import com.acv.chat.data.openai.FileSource
import com.acv.chat.data.openai.ModelId
import com.acv.chat.data.openai.assistant.Assistant
import com.acv.chat.data.openai.assistant.AssistantApi
import com.acv.chat.data.openai.assistant.file.Purpose
import com.acv.chat.data.openai.assistant.message.ThreadMessageApi
import com.acv.chat.data.openai.assistant.runs.RunApi
import com.acv.chat.data.openai.assistant.runs.actionSolver
import com.acv.chat.data.openai.assistant.runs.add
import com.acv.chat.data.openai.assistant.runs.codeInterpreter
import com.acv.chat.data.openai.assistant.runs.invoke
import com.acv.chat.data.openai.assistant.runs.retrieval
import com.acv.chat.data.openai.assistant.thread.ThreadApi
import com.acv.chat.data.openai.file.FileUpload
import com.acv.chat.data.openai.file.FilesApi
import com.acv.chat.data.weather.WeatherInfo
import com.acv.chat.data.weather.WeatherRepository
import okio.Source

interface AssistantWeather {

  context(Raise<DomainError>)
  suspend fun getWeather(): String

  context(Raise<DomainError>)
  suspend fun updateAssistant(source: Source): Assistant
}

context(WeatherRepository, AssistantApi, ThreadApi, RunApi, ThreadMessageApi, FilesApi)
class AssistantWeatherImpl : AssistantWeather {

  private val actionSolver = actionSolver {
    add<WeatherInfo> { weather ->
      orNull { getWeatherData(weather.latitude, weather.longitude) }
    }
    add(codeInterpreter)
    add(retrieval)
  }

  context(Raise<DomainError>)
  override suspend fun getWeather(): String = with(actionSolver) {
//    val file = fileApi.file(FileUpload(file = FileSource(source = "asdfasdf".asSource(), name = "asdfasdf"), purpose = Purpose.assistants))
    var assistant = assistants().data.getOrNull(0) ?: createAssistant(model = ModelId.Gpt4Preview)
//    assistant = modify(assistant.id, model = ModelId.Gpt4Preview, files = listOf(file.id))
    val thread = createThread()
    val message = createMessage(prompt = "Tell me about the file i provide you", threadId = thread.id)
    val run = createRun(assistantId = assistant.id, threadId = thread.id)
    run()

    return all(thread.id).data.first().content.first().text.value
  }

  context(Raise<DomainError>)
  override suspend fun updateAssistant(source: Source): Assistant {
    val file = FileUpload(FileSource(name = "mydoc", source = source), purpose = Purpose.assistants)
    val id = file(file).id
    return modify(assistantId = assistants().data.first().id, model = ModelId.Gpt4Preview, files = listOf(id))
  }

}