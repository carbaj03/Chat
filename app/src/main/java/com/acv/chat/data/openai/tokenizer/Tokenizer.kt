package com.acv.chat.data.openai.tokenizer

import com.acv.chat.data.openai.common.ModelId

interface Tokenizer {

  fun encode(
    text: String,
    allowedSpecial: Set<String> = emptySet(),
    disallowedSpecial: Set<String> = setOf("all"),
  ): List<Int>

  fun encodeSingleToken(text: String): Int

  fun decode(tokens: List<Int>): String

  fun decode(token: Int): String

  companion object {

    context (BpeLoader)
    suspend fun of(model: ModelId): Tokenizer {
      val config: EncodingConfig = EncodingConfig.of(model)
      return from(config)
    }

    private fun from(config: EncodingConfig): Tokenizer {
      val coreBPE = CoreBPE.create(
        encoder = config.mergeableRanks,
        specialTokensEncoder = config.specialTokens,
        pattern = config.pattern
      )
      val specialTokensSet = config.specialTokens.keys.map { it.utf8() }.toSet()
      return TokenEncoder(bpe = coreBPE, specialTokensSet = specialTokensSet)
    }
  }
}

context (BpeLoader)
suspend fun ModelId.tokenizer(): Tokenizer =
  Tokenizer.of(this)
