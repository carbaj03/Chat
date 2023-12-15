package com.acv.chat.data.weather

import arrow.core.raise.Raise

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
  suspend fun getWeather(lat: Double, long: Double): String
}