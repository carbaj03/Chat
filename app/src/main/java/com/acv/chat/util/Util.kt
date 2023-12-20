package com.acv.chat.util

@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
public inline fun <C, R> C.ifNotEmpty(defaultValue: () -> R): R where C : CharSequence, C : R =
  if (isNotEmpty()) defaultValue() else this
