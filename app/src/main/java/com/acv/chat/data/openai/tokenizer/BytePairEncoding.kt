package com.acv.chat.data.openai.tokenizer

import okio.ByteString

internal fun bytePairEncode(piece: ByteString, ranks: Map<ByteString, Int>): IntArray {
  if (piece.size == 1) {
    val value = ranks[piece] ?: 0
    return intArrayOf(value)
  }

  val parts = MutableList(piece.size + 1) { index ->
    IntArray(2) { if (it == 0) index else Int.MAX_VALUE }
  }

  for (i in 0 until parts.size - 2) {
    parts[i][1] = getRank(piece, ranks, parts, i, 0) ?: Int.MAX_VALUE
  }

  while (parts.size > 1) {
    val index = findMinRankIndex(parts) ?: break
    parts[index][1] = getRank(piece, ranks, parts, index, 1) ?: Int.MAX_VALUE
    if (index > 0) {
      val prevIndex = index - 1
      parts[prevIndex][1] = getRank(piece, ranks, parts, prevIndex, 1) ?: Int.MAX_VALUE
    }
    parts.removeAt(index + 1)
  }

  return IntArray(parts.size - 1) { i ->
    val start = parts[i][0]
    val end = parts[i + 1][0]
    ranks[piece.substring(start, end)] ?: 0
  }
}

private fun getRank(
  piece: ByteString,
  ranks: Map<ByteString, Int>,
  parts: MutableList<IntArray>,
  startIndex: Int,
  skip: Int
): Int? {
  if (startIndex + skip + 2 >= parts.size) return null
  val bytes = piece.substring(parts[startIndex][0], parts[startIndex + skip + 2][0])
  return ranks[bytes]
}

private fun findMinRankIndex(parts: List<IntArray>): Int? {
  var minRank = Int.MAX_VALUE
  var minIdx = -1
  for (i in parts.indices) {
    if (parts[i][1] < minRank) {
      minRank = parts[i][1]
      minIdx = i
    }
  }
  return if (minRank < Int.MAX_VALUE) minIdx else null
}
