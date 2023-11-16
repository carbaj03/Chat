package com.acv.chat.data.openai.audio

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.openai.OpenAIClient
import com.acv.chat.data.openai.chat.appendFileSource
import com.acv.chat.domain.Audio
import com.acv.chat.domain.DomainError
import com.acv.chat.domain.Transcription
import com.acv.chat.domain.Translation
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.content.PartData
import io.ktor.http.isSuccess
import okio.FileSystem
import okio.Path.Companion.toPath

interface AudioService {

  context(Raise<DomainError>)
  suspend fun transcript(audio: Audio): Transcription

  context(Raise<DomainError>)
  suspend fun translation(audio: Audio): Translation
}

context(OpenAIClient)
class AudioOpenAI : AudioService {

  context(Raise<DomainError>)
  override suspend fun transcript(audio: Audio): Transcription =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val path = audio.path.toPath()
      val request = TranscriptionRequest(audio = FileSource(name = path.name, source = FileSystem.SYSTEM.source(path)))
      val response = client.submitFormWithBinaryData(url = "audio/transcriptions", formData = request.formData())
      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }
      response.body<TranscriptionOAI>().text.let(::Transcription)
    }

  private fun TranscriptionRequest.formData(): List<PartData> =
    formData {
      appendFileSource(key = "file", fileSource = audio)
      append(key = "model", value = model.id)
      prompt?.let { prompt -> append(key = "prompt", value = prompt) }
      responseFormat?.let { append(key = "response_format", value = it.value) }
      temperature?.let { append(key = "temperature", value = it) }
      language?.let { append(key = "language", value = it.iso6391) }
    }

  context(Raise<DomainError>)
  override suspend fun translation(audio: Audio): Translation =
    catch(
      onError = { raise(DomainError.UnknownDomainError(it)) }
    ) {
      val path = audio.path.toPath()
      val request = TranslationRequest(audio = FileSource(name = path.name, source = FileSystem.SYSTEM.source(path)))
      val response = client.submitFormWithBinaryData(url = "audio/translations", formData = request.formData())

      ensure(response.status.isSuccess()) {
        raise(DomainError.NetworkDomainError(response.status.value, response.bodyAsText()))
      }
      response.body<TranscriptionOAI>().text.let(::Translation)
    }

  private fun TranslationRequest.formData(): List<PartData> =
    formData {
      appendFileSource("file", audio)
      append(key = "model", value = model.id)
      prompt?.let { prompt -> append(key = "prompt", value = prompt) }
      responseFormat?.let { append(key = "response_format", value = it) }
      temperature?.let { append(key = "temperature", value = it) }
    }
}