package com.acv.chat.data.openai.assistant

import io.ktor.http.HeadersBuilder

fun HeadersBuilder.beta() {
  append("OpenAI-Beta", "assistants=v1")
}