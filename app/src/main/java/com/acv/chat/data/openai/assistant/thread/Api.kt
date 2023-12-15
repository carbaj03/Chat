package com.acv.chat.data.openai.assistant.thread

import arrow.core.raise.Raise
import com.acv.chat.data.openai.assistant.get
import com.acv.chat.data.openai.assistant.post
import com.acv.chat.data.openai.assistant.runs.ThreadId
import com.acv.chat.domain.DomainError
import com.acv.chat.data.openai.common.OpenAIClient
import com.acv.chat.data.openai.chat.Counter

context(Counter, OpenAIClient)
class ThreadApi {

  context(Raise<DomainError>)
  suspend fun createThread(): Thread =
    post(url = "threads", request = ThreadRequest())

  context(Raise<DomainError>)
  suspend fun get(
    threadId: ThreadId
  ): Thread =
    get(url = "threads/${threadId.id}")
}