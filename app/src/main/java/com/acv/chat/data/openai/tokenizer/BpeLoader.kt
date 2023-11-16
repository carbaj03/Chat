package com.acv.chat.data.openai.tokenizer

import okio.ByteString
import okio.ByteString.Companion.decodeBase64
import okio.FileSystem

sealed interface BpeLoader {
  suspend fun loadEncoding(encoding: Encoding): Map<ByteString, Int>
}

internal fun defaultPbeLoader(): BpeLoader =
  LocalPbeLoader(FileSystem.RESOURCES)

internal fun loadTiktokenBpe(data: ByteArray): Map<ByteString, Int> {
  val bpeRanks = mutableMapOf<ByteString, Int>()
  val lines = data.decodeToString().split("\n")
  for (line in lines) {
    if (line.isEmpty()) continue
    val (encodedToken, rankString) = line.split(" ")
    val token = encodedToken.decodeBase64() ?: error("can't decode $encodedToken")
    val rank = rankString.toInt()
    bpeRanks[token] = rank
  }
  return bpeRanks
}