package com.acv.chat.arrow

import arrow.core.raise.Raise
import arrow.core.raise.either
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
inline fun <A, Error> orNull(@BuilderInference block: Raise<Error>.() -> A): A? =
  either(block).fold({ null }, { it })

