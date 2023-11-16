package com.acv.chat.data.openai.chat

import com.acv.chat.data.openai.ModelId
import com.acv.chat.data.openai.tokenizer.BpeLoader
import com.acv.chat.data.openai.tokenizer.tokenizer

context(BpeLoader)
class Counter private constructor(
  private var numTokens: Int = 3,
  private val tokensPerMessage: Int = 4,
  private val tokensPerName: Int = 1,
) {

  suspend fun countTokens(modelId: ModelId, messages: List<ChatMessage>): Int {
    numTokens = 3
    val tokenizer = modelId.tokenizer()
    for (message in messages) {
      numTokens += tokensPerMessage
      message.content?.let { numTokens += tokenizer.encode(it).size }
      message.name?.let { numTokens += tokensPerName }
    }
    println("Tokens: $numTokens")
    return numTokens
  }

  suspend fun countTokens(modelId: ModelId, chatMessage: ChatMessage): Int {
    val tokenizer = modelId.tokenizer()
    chatMessage.content?.let { numTokens += tokenizer.encode(it).size }
    chatMessage.name?.let { numTokens += tokensPerName }
    println("Tokens: $numTokens")
    return numTokens
  }

  companion object {

    context (BpeLoader)
    operator fun invoke(): Counter {
      return Counter(numTokens = 3, tokensPerMessage = 4, tokensPerName = 1)
    }
  }
}
