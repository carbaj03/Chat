package com.acv.chat.data.openai.assistant.runs

import arrow.core.raise.Raise
import com.acv.chat.data.openai.assistant.AssistantApi
import com.acv.chat.data.openai.assistant.message.ThreadMessageApi
import com.acv.chat.data.openai.assistant.thread.ThreadApi
import com.acv.chat.domain.DomainError
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

context(Raise<DomainError>, RunApi, AssistantApi, ThreadApi, ThreadMessageApi, ActionSolver)
tailrec suspend operator fun Run.invoke() {
  when (status) {
    Run.Status.InProgress -> {
      delay(5.seconds)
      getRun(threadId, id)()
    }
    Run.Status.Queued -> {
      delay(5.seconds)
      getRun(threadId, id)()
    }
    Run.Status.RequiresAction -> {
      val outputs = requiredAction?.submit_tool_outputs?.tool_calls?.invoke() ?: emptyList()
      val r1 = submitToolOutputs(
        treadId = threadId,
        runId = id,
        outputs = outputs
      )
      getRun(r1.threadId, r1.id)()
    }
    Run.Status.Cancelled,
    Run.Status.Cancelling,
    Run.Status.Completed,
    Run.Status.Expired,
    Run.Status.Failed -> {
    }
  }
}