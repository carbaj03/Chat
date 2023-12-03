package com.acv.chat.data.weather

import android.util.Log
import arrow.core.raise.Raise
import arrow.core.raise.ensure
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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class WeatherInfo(
  val latitude : Double,
  val longitude : Double,
  val temperature: String? = null,
  @Description("fahrenheit or celsius") val unit: String? = null
)

class WeatherRepositoryImpl(
  private val api: WeatherApi
) : WeatherRepository {

  context(Raise<WeatherRepository.Error>)
  override suspend fun getWeatherData(lat: Double, long: Double): String =
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
    val response = arrow.core.raise.catch({
      openMeteo.get("forecast") {
        url {
          parameters.append("hourly", OPEN_METEO_ARGS)
          parameters.append("latitude", "$latitude")
          parameters.append("longitude", "$longitude")
        }
      }
    }) { raise(WeatherRepository.Error.NetworkError(null, it.message.orEmpty())) }
    ensure(response.status.isSuccess()) {
      raise(WeatherRepository.Error.NetworkError(response.status.value, response.bodyAsText()))
    }
    return arrow.core.raise.catch<IllegalArgumentException, _>({
      response.body<WeatherDto>()
    }) { raise(WeatherRepository.Error.SerializationError(it)) }
  }
}

@Serializable
data class WeatherDto(
  @SerialName("hourly")
  val weatherData: WeatherDataDto
)

@Serializable
data class WeatherDataDto(
  val time: List<String>,
  @SerialName("temperature_2m")
  val temperature: List<Double>,
  @SerialName("relativehumidity_2m")
  val humidities: List<Double>,
  @SerialName("pressure_msl")
  val pressures: List<Double>,
  @SerialName("windspeed_10m")
  val windSpeeds: List<Double>,
  @SerialName("weathercode")
  val weatherCodes: List<Int>
)

interface WeatherRepository {
  sealed interface Error {
    val message: String

    data class NetworkError(
      val statusCode: Int?,
      override val message: String
    ) : Error

    data class SerializationError(
      val error: IllegalArgumentException
    ) : Error {
      override val message: String = error.message ?: error.toString()
    }
  }

  context(Raise<Error>)
  suspend fun getWeatherData(lat: Double, long: Double): String
}