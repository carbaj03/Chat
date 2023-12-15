package com.acv.chat.data.weather

import android.util.Log
import arrow.core.raise.Raise
import arrow.core.raise.ensure
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.schema.Description
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class WeatherInfo(
  val latitude: Double,
  val longitude: Double,
  val temperature: String? = null,
  @Description("fahrenheit or celsius") val unit: String? = null
)

class WeatherRepositoryImpl(
  private val api: WeatherApi
) : WeatherRepository {

  context(Raise<WeatherRepository.Error>)
  override suspend fun getWeather(lat: Double, long: Double): String =
    api.getWeatherData(lat, long).weatherData.temperature.first().toString()
}

private const val OPEN_METEO_ARGS = "temperature_2m,relativehumidity_2m,weathercode,pressure_msl,windspeed_10m"

class WeatherApi : AutoCloseable {
  private val openMeteo = HttpClient {
    defaultRequest {
      url("https://api.open-meteo.com/v1/")
    }
    install(ContentNegotiation) {
      json(
        Json {
          ignoreUnknownKeys = true
        }
      )
    }
    install(HttpRequestRetry) {
      retryOnServerErrors(maxRetries = 5)
      exponentialDelay()
    }
    install(Logging) {
      level = LogLevel.ALL
      logger = object : Logger {
        override fun log(message: String) {
          Log.d("weather", message)
        }
      }
      sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }

  }

  override fun close() {
    openMeteo.close()
  }

  context(Raise<WeatherRepository.Error>)
  suspend fun getWeatherData(
    latitude: Double,
    longitude: Double
  ): WeatherDto {
    val response = catch(
      onError = { WeatherRepository.Error.NetworkError(null, it) }
    ) {
      openMeteo.get("forecast") {
        url {
          parameters.append("hourly", OPEN_METEO_ARGS)
          parameters.append("latitude", "$latitude")
          parameters.append("longitude", "$longitude")
        }
      }
    }

    ensure(response.status.isSuccess()) {
      raise(WeatherRepository.Error.NetworkError(response.status.value, response.bodyAsText()))
    }

    return catch(
      onError = { WeatherRepository.Error.SerializationError(IllegalArgumentException()) }
    ) {
      response.body<WeatherDto>()
    }
  }
}

