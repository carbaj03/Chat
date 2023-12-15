package com.acv.chat.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.acv.chat.data.openai.common.OpenAIClient
import com.acv.chat.data.openai.assistant.AssistantApi
import com.acv.chat.data.openai.assistant.file.FileAssistantApi
import com.acv.chat.data.openai.assistant.message.ThreadMessageApi
import com.acv.chat.data.openai.assistant.runs.RunApi
import com.acv.chat.data.openai.assistant.step.RunStepApi
import com.acv.chat.data.openai.assistant.thread.ThreadApi
import com.acv.chat.data.openai.audio.AudioOpenAI
import com.acv.chat.data.openai.audio.AudioService
import com.acv.chat.data.openai.audio.AudioServiceMock
import com.acv.chat.data.openai.chat.ChatApi
import com.acv.chat.data.openai.chat.ChatService
import com.acv.chat.data.openai.chat.ChatServiceMock
import com.acv.chat.data.openai.chat.Counter
import com.acv.chat.data.openai.file.FilesApi
import com.acv.chat.data.openai.model.ModelApi
import com.acv.chat.data.openai.tokenizer.AndroidPbeLoader
import com.acv.chat.data.openai.tokenizer.BpeLoader
import com.acv.chat.data.weather.WeatherApi
import com.acv.chat.data.weather.WeatherRepository
import com.acv.chat.data.weather.WeatherRepositoryImpl
import com.acv.chat.util.TempFileProvider
import com.acv.chat.util.rememberFileProvider
import kotlinx.coroutines.CoroutineScope
import with

interface Dependencies {

  val optics: AppOptics

  val photoService: PhotoService
  val mediaPicker: MediaService
  val documentService: DocumentService
  val audioRecorder: AudioRecorder
  val audioPlayer: AudioPlayer
  val speechRecognizer: SpeechRecognizer
  val bpeLoader: BpeLoader
  val counter: Counter
  val chat: ChatService
  val models: ModelApi
  val audio: AudioService

  val assistantApi: AssistantApi
  val threadApi: ThreadApi
  val threadMessageApi: ThreadMessageApi
  val runApi: RunApi
  val stepApi: RunStepApi
  val fileAssistantApi: FileAssistantApi
  val fileApi: FilesApi

  val weatherRepository: WeatherRepository
  val assistantWeather: AssistantWeather

  val analytics: Analytics

  val store: Store<App>
}

context(CoroutineScope)
class DependenciesMock(
  override val optics: AppOptics = AppOptics,
  override val photoService: PhotoService = PhotoServiceMock(),
  override val mediaPicker: MediaService = MediaServiceMock(),
  override val documentService: DocumentService = DocumentServiceMock(),
  override val audioPlayer: AudioPlayer = AudioPlayerMock(),
  override val audioRecorder: AudioRecorder = AudioRecorderMock(),
  override val chat: ChatService = ChatServiceMock(),
  override val audio: AudioService = AudioServiceMock(),
) : Dependencies {

  override val speechRecognizer: SpeechRecognizer
    get() = TODO("Not yet implemented")
  override val bpeLoader: BpeLoader
    get() = TODO("Not yet implemented")
  override val counter: Counter
    get() = TODO("Not yet implemented")
  override val models: ModelApi
    get() = TODO("Not yet implemented")
  override val assistantApi: AssistantApi
    get() = TODO("Not yet implemented")
  override val threadApi: ThreadApi
    get() = TODO("Not yet implemented")
  override val threadMessageApi: ThreadMessageApi
    get() = TODO("Not yet implemented")
  override val runApi: RunApi
    get() = TODO("Not yet implemented")
  override val stepApi: RunStepApi
    get() = TODO("Not yet implemented")
  override val fileAssistantApi: FileAssistantApi
    get() = TODO("Not yet implemented")
  override val fileApi: FilesApi
    get() = TODO("Not yet implemented")
  override val weatherRepository: WeatherRepository
    get() = TODO("Not yet implemented")
  override val assistantWeather: AssistantWeather
    get() = TODO("Not yet implemented")

  override val analytics: Analytics = AnalyticsMock()

  override val store: Store<App> = App()
}

@Composable
fun rememberDependencies(): Dependencies {
  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  val fileProvider: TempFileProvider = rememberFileProvider()
  val documentService = rememberDocumentLauncher(fileProvider)
  val photoService: PhotoService = rememberPhotoLauncher(fileProvider)
  val mediaPicker: MediaService = rememberGalleryLauncher(fileProvider)

  return remember {
    object : Dependencies {
      override val optics: AppOptics = AppOptics

      override val photoService: PhotoService = photoService
      override val mediaPicker: MediaService = mediaPicker
      override val documentService: DocumentService = documentService
      override val audioRecorder: AudioRecorder = AndroidAudioRecorder(context)
      override val audioPlayer: AudioPlayer = AndroidAudioPlayer(context)
      override val speechRecognizer: SpeechRecognizer = SpeechRecognizerAndroid(context)
      override val bpeLoader: BpeLoader = AndroidPbeLoader(context)
      override val counter: Counter = with(bpeLoader) { Counter() }
      override val chat: ChatService = with(counter, OpenAIClient) { ChatApi() }
      override val models: ModelApi = with(OpenAIClient) { ModelApi() }
      override val audio: AudioService = with(OpenAIClient) { AudioOpenAI() }

      override val assistantApi: AssistantApi = with(counter, OpenAIClient) { AssistantApi() }
      override val threadApi: ThreadApi = with(counter, OpenAIClient) { ThreadApi() }
      override val threadMessageApi: ThreadMessageApi = with(counter, OpenAIClient) { ThreadMessageApi() }
      override val runApi: RunApi = with(counter, OpenAIClient) { RunApi() }
      override val stepApi: RunStepApi = with(counter, OpenAIClient) { RunStepApi() }
      override val fileAssistantApi: FileAssistantApi = with(counter, OpenAIClient) { FileAssistantApi() }
      override val fileApi: FilesApi = with(OpenAIClient) { FilesApi() }

      override val weatherRepository: WeatherRepository = WeatherRepositoryImpl(WeatherApi())
      override val assistantWeather: AssistantWeather = with(weatherRepository, assistantApi, threadApi, fileApi, threadMessageApi, runApi) { AssistantWeatherImpl() }

      override val analytics: Analytics = FirebaseImpl(context)

      override val store: Store<App> = with(scope) { App() }
    }
  }
}