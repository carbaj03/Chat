package com.acv.chat.data.openai

import android.util.Log
import com.acv.chat.BuildConfig
import com.acv.chat.data.openai.chat.JsonLenient
import io.ktor.client.HttpClient
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.Duration.Companion.seconds

object OpenAIClient : AutoCloseable {
  private const val API_KEY = BuildConfig.API_ENDPOINT

  val client = HttpClient {
    defaultRequest {
      url("https://api.openai.com/v1/")
    }
    install(ContentNegotiation) {
      json(JsonLenient)
    }
    install(Auth) {
      bearer {
        loadTokens {
          BearerTokens(accessToken = API_KEY, refreshToken = "")
        }
      }
    }
    install(HttpTimeout) {
      socketTimeoutMillis = 30.seconds.inWholeMilliseconds
      connectTimeoutMillis = 30.seconds.inWholeMilliseconds
      requestTimeoutMillis = 30.seconds.inWholeMilliseconds
    }
    install(HttpRequestRetry) {
      retryOnServerErrors(maxRetries = 5)
      exponentialDelay()
    }
    install(Logging) {
      level = LogLevel.ALL
      logger = object : Logger {
        override fun log(message: String) {
          Log.d("ktor", message)
        }
      }
      sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
    expectSuccess = true
  }

  override fun close() {
    client.close()
  }
}