package com.acv.chat.data.openai.tokenizer

import com.acv.chat.data.openai.common.ModelId
import okio.ByteString

class EncodingConfig(
  val pattern: Regex,
  val mergeableRanks: Map<ByteString, Int>,
  val specialTokens: Map<ByteString, Int>,
  val explicitNVocab: Int? = null,
) {
  init {
    if (explicitNVocab != null) {
      val totalCount = mergeableRanks.size + specialTokens.size
      require(totalCount == explicitNVocab) { "the expected number of tokens in the vocabulary is incorrect, expected: $explicitNVocab, actual: $totalCount" }
    }
  }

  companion object {
    private val cache: EncodingStore = EncodingStore()

    context(BpeLoader)
    suspend fun of(model: ModelId): EncodingConfig {
      val encoding: Encoding = modelToEncoding[model] ?: error("no encoding for model $model")
      return cache.get(encoding) ?: run {
        val ranks = loadEncoding(encoding)
        encoding.encodingConfig(ranks).also { cache.put(encoding, it) }
      }
    }
  }
}
