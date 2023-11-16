package com.acv.chat.data.openai.tokenizer

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class EncodingStore {

  private val map = mutableMapOf<Encoding, EncodingConfig>()

  private val mutex = Mutex()

  suspend fun get(key: Encoding): EncodingConfig? {
    return mutex.withLock {
      map[key]
    }
  }

  suspend fun put(key: Encoding, value: EncodingConfig): EncodingConfig? {
    return mutex.withLock {
      map.put(key, value)
    }
  }
}
