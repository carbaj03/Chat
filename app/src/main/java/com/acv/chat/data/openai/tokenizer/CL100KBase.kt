package com.acv.chat.data.openai.tokenizer

import okio.ByteString

data object CL100KBase: Encoding {

  override fun encodingConfig(ranks: Map<ByteString, Int>): EncodingConfig {
    val specialTokens = mapOf(
      Tokens.ENDOFTEXT to 100257,
      Tokens.FIM_PREFIX to 100258,
      Tokens.FIM_MIDDLE to 100259,
      Tokens.FIM_SUFFIX to 100260,
      Tokens.ENDOFPROMPT to 100276,
    )
    return EncodingConfig(
      pattern = Patterns.P100K,
      mergeableRanks = ranks,
      specialTokens = specialTokens,
    )
  }
}
