package com.acv.chat.domain

import arrow.core.raise.Raise

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

context(Raise<DomainError>)
fun DomainError.raise(): DomainError =
  raise(this)