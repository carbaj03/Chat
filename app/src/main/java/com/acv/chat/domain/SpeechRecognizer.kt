package com.acv.chat.domain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH
import android.speech.RecognizerIntent.EXTRA_LANGUAGE
import android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL
import android.speech.RecognizerIntent.EXTRA_MAX_RESULTS
import android.speech.RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS
import android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
import arrow.core.raise.Raise
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.time.Duration.Companion.minutes

interface SpeechRecognizer {
  val isAvailable: Boolean

  context(Raise<DomainError>)
  suspend fun startSpeech() : String?

  context(Raise<DomainError>)
  suspend fun stopSpeech(): String?
}

class SpeechRecognizerMock : SpeechRecognizer {
  context(Raise<DomainError>)
  override suspend fun startSpeech(): String? {
    return ""
  }

  context(Raise<DomainError>)
  override suspend fun stopSpeech(): String? = null

  override val isAvailable: Boolean = false
}

class SpeechRecognizerAndroid(
  private val context: Context,
  private val dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : SpeechRecognizer {

  private var result: CompletableDeferred<String?>? = null
  private var speechRecognizer: android.speech.SpeechRecognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(context)
  var available: Boolean = true
  override val isAvailable: Boolean get() = available

  context(Raise<DomainError>)
  override suspend fun stopSpeech(): String = withContext(dispatcher) {
    result?.await().also { result = null } ?: raise(DomainError.UnknownDomainError("SpeechRecognizer is not available"))
  }

  context(Raise<DomainError>)
  override suspend fun startSpeech(): String? = withContext(dispatcher) {
    result = CompletableDeferred()

    val listener = object : RecognitionListener {
      override fun onReadyForSpeech(p0: Bundle?) {
      }

      override fun onBeginningOfSpeech() {
        available = false
      }

      override fun onRmsChanged(p0: Float) {
      }

      override fun onBufferReceived(p0: ByteArray?) {
      }

      override fun onEndOfSpeech() {
        available = true
      }

      override fun onError(error: Int) {
//        result?.complete(null)
      }

      override fun onResults(results: Bundle?) {
        results?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.let { result?.complete(it) }.also { result = null }
      }

      override fun onPartialResults(result: Bundle?) {
      }

      override fun onEvent(p0: Int, p1: Bundle?) {
      }
    }

    val speechRecognizerIntent = Intent(ACTION_RECOGNIZE_SPEECH).apply {
      putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM)
      putExtra(EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1.minutes.inWholeMilliseconds)
      putExtra(EXTRA_MAX_RESULTS, 1)
      putExtra(EXTRA_LANGUAGE, Locale.getDefault())
    }

    speechRecognizer?.setRecognitionListener(listener)
    speechRecognizer?.startListening(speechRecognizerIntent)

    result?.await()
  }
}