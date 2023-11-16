package com.acv.chat

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import arrow.core.raise.Raise
import arrow.core.raise.effect
import arrow.core.raise.fold
import com.acv.chat.components.Surface
import com.acv.chat.components.Theme
import com.acv.chat.components.invoke
import com.acv.chat.data.openai.OpenAIClient
import com.acv.chat.data.openai.Parameters
import com.acv.chat.data.openai.assistant.AssistantApi
import com.acv.chat.data.openai.assistant.Tool
import com.acv.chat.data.openai.assistant.message.ThreadMessageApi
import com.acv.chat.data.openai.assistant.runs.RunApi
import com.acv.chat.data.openai.assistant.runs.RunObjectBeta
import com.acv.chat.data.openai.assistant.runs.Status.cancelled
import com.acv.chat.data.openai.assistant.runs.Status.completed
import com.acv.chat.data.openai.assistant.runs.Status.failed
import com.acv.chat.data.openai.assistant.runs.Status.inProgress
import com.acv.chat.data.openai.assistant.runs.Status.queued
import com.acv.chat.data.openai.assistant.runs.Status.requiresAction
import com.acv.chat.data.openai.assistant.runs.ToolOutput
import com.acv.chat.data.openai.assistant.step.RunStepApi
import com.acv.chat.data.openai.assistant.thread.ThreadApi
import com.acv.chat.data.openai.audio.AudioOpenAI
import com.acv.chat.data.openai.audio.AudioService
import com.acv.chat.data.openai.chat.ChatApi
import com.acv.chat.data.openai.chat.ChatService
import com.acv.chat.data.openai.chat.Counter
import com.acv.chat.data.openai.model.ModelApi
import com.acv.chat.data.openai.tokenizer.AndroidPbeLoader
import com.acv.chat.data.schema.Description
import com.acv.chat.data.schema.buildJsonSchema
import com.acv.chat.domain.AndroidAudioPlayer
import com.acv.chat.domain.AndroidAudioRecorder
import com.acv.chat.domain.App
import com.acv.chat.domain.DomainError
import com.acv.chat.domain.MediaService
import com.acv.chat.domain.PhotoService
import com.acv.chat.domain.SpeechRecognizerAndroid
import com.acv.chat.domain.rememberGalleryLauncher
import com.acv.chat.domain.rememberPhotoLauncher
import com.acv.chat.domain.screen
import com.acv.chat.domain.screen.Home
import com.acv.chat.domain.screen.createHome
import com.acv.chat.domain.screen.invoke
import com.acv.chat.ui.theme.invoke
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import with

@Serializable
data class WeatherInfo(
  val location: String,
  val temperature: String? = null,
  @Description("fahrenheit or celsius") val unit: String? = null
)

@OptIn(ExperimentalSerializationApi::class)
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)

    setContent {
      val scope = rememberCoroutineScope()
      val photographer: PhotoService = rememberPhotoLauncher()
      val mediaPicker: MediaService = rememberGalleryLauncher()
      val audioRecorder: AndroidAudioRecorder = remember { AndroidAudioRecorder(this) }
      val audioPlayer = remember { AndroidAudioPlayer(this) }
      val speechRecognizer = remember { SpeechRecognizerAndroid(this) }
      val bpeLoader = remember { AndroidPbeLoader(this) }
      val counter = remember { with(bpeLoader) { Counter() } }
      val chat: ChatService = remember { with(counter, OpenAIClient) { ChatApi() } }
      val models = remember { with(OpenAIClient) { ModelApi() } }
      val audio: AudioService = remember { with(OpenAIClient) { AudioOpenAI() } }

      val assistantApi = remember { with(counter, OpenAIClient) { AssistantApi() } }
      val threadApi = remember { with(counter, OpenAIClient) { ThreadApi() } }
      val threadMessageApi = remember { with(counter, OpenAIClient) { ThreadMessageApi() } }
      val runApi = remember { with(counter, OpenAIClient) { RunApi() } }
      val stepApi = remember { with(counter, OpenAIClient) { RunStepApi() } }

      LaunchedEffect(Unit) {
        effect {
//          fun function(name: String, description: String, parameters: JsonObjectBuilder.() -> Unit): Tool {
//            return tool(name, description, Parameters.buildJsonObject(parameters))
//          }
//
//          val weather = function("weather", "Get the current weather", parameters = {
//            put("type", "object")
//            putJsonObject("properties") {
//              putJsonObject("location") {
//                put("type", "string")
//                put("description", "The city and state, e.g. San Francisco, CA")
//              }
//              putJsonObject("unit") {
//                put("type", "string")
//                putJsonArray("enum") {
//                  add("celsius")
//                  add("fahrenheit")
//                }
//              }
//            }
//            putJsonArray("required") {
//              add("location")
//            }
//          })

          val serializer = serializer<WeatherInfo>()
          val descriptor = serializer.descriptor
          val parameters = buildJsonSchema(descriptor)
          val fnName = descriptor.serialName.substringAfterLast(".").lowercase()

          val weather: Tool = Tool.function(
            name = fnName,
            description = "Generated function for $fnName",
            parameters = Parameters(parameters)
          )

          with(assistantApi, threadApi, threadMessageApi, runApi) {

            val assistant = assistants().data.getOrNull(0) ?: createAssistant(tools = listOf(weather, Tool.CodeInterpreter))
            val thread = createThread()
            val message = createMessage(prompt = "The current weather in Madrid", threadId = thread.id)
            val run = createRun(assistantId = assistant.id.value, threadId = thread.id)

            make(run, thread.id, fnName)

            val response = threadMessageApi.all(thread.id).data.first().content.first().text.value
            println(response)
          }
        }.fold(
          { println(it) },
          { }
        )
      }

      val app = remember {
        with(scope, photographer, mediaPicker, speechRecognizer, audioRecorder, audioPlayer, chat, audio) {
          App {
            delay(1000)
            screen set createHome()
          }
        }
      }

      val state by app.state.collectAsState()

      state.theme {
        state.theme.surface(modifier = Modifier.fillMaxSize()) {

          when (val Screen = state.screen) {
            null -> Text(text = "Loading...")
            is Home -> Screen()
          }
        }
      }
    }
  }
}

context(RunApi, AssistantApi, ThreadApi, ThreadMessageApi, Raise<DomainError>)
tailrec suspend fun make(r: RunObjectBeta, threadId: String, fnName: String) {
  when (r.status) {
    queued -> {
      println("status : ${r.status}")
      delay(2000)
      make(getRun(threadId, r.id), threadId, fnName)
    }
    inProgress -> {
      println("status : ${r.status}")
      delay(2000)
      make(getRun(threadId, r.id), threadId, fnName)
    }
    failed -> {
      println("status : failed")
    }
    completed -> {
      println("status : completed")
    }
    requiresAction -> {
      println("status : requires_action")
      val outputs = mutableListOf<ToolOutput>()
      r.required_action?.submit_tool_outputs?.tool_calls?.forEach {
        println(it.function.name + " : " + fnName)
        println(it.function.arguments)

        when (it.function.name) {
          fnName -> {
//            println(Json.decodeFromString(serializer, it.function.arguments))
            outputs.add(ToolOutput(it.id, "30ª"))
          }
          else -> {
            outputs.add(ToolOutput(it.id, "error"))
          }
        }
      }

      val r1 = submit_tool_outputs(threadId, r.id, outputs = outputs)
      make(r1, threadId, fnName)
    }
    cancelled -> {
      println("status : ${r.status}")
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  val appState = App(screen = null, theme = Theme.Light(Surface()))

  appState.theme {
    Greeting("Android")
  }
}