package com.acv.chat.domain

sealed interface DomainError {
  data object Default : DomainError
  data object NotFile : DomainError

  data class UnknownDomainError(
    val error: String
  ) : DomainError

  data class NetworkDomainError(
    val statusCode: Int?,
    val message: String
  ) : DomainError
}