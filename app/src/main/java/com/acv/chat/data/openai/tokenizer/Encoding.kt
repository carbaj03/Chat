package com.acv.chat.data.openai.tokenizer

import com.acv.chat.data.openai.ModelId
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8

sealed interface Encoding {
  fun encodingConfig(ranks: Map<ByteString, Int>): EncodingConfig
}

private const val patternP100K = """'s|'t|'re|'ve|'m|'ll|'d|[^\r\n\p{L}\p{N}]?\p{L}+|\p{N}{1,3}| ?[^\s\p{L}\p{N}]+[\r\n]*|\s*[\r\n]+|\s+(?!\S)|\s+"""
private const val patternP100KUnicode = "(?U)$patternP100K"

internal val regexP100K: Regex
  get() = try {
    Regex(patternP100KUnicode, RegexOption.IGNORE_CASE)
  } catch (e: IllegalArgumentException) {
    Regex(patternP100K, RegexOption.IGNORE_CASE)
  }

internal object Patterns {
  val P100K: Regex = regexP100K
}

internal object Tokens {
  val ENDOFTEXT = "<|endoftext|>".encodeUtf8()
  val FIM_PREFIX = "<|fim_prefix|>".encodeUtf8()
  val FIM_MIDDLE = "<|fim_middle|>".encodeUtf8()
  val FIM_SUFFIX = "<|fim_suffix|>".encodeUtf8()
  val ENDOFPROMPT = "<|endofprompt|>".encodeUtf8()
}

internal val modelToEncoding: Map<ModelId, Encoding> = mapOf(
  ModelId.Gpt4Vision to CL100KBase,
  ModelId.Gpt4 to CL100KBase,
  ModelId.Gpt35 to CL100KBase,
  ModelId.TextEmbeddingAda002 to CL100KBase,
)