package com.acv.chat.data.openai.assistant.runs

import arrow.core.raise.Raise
import com.acv.chat.data.openai.assistant.ActionSolver
import com.acv.chat.data.openai.assistant.AssistantApi
import com.acv.chat.data.openai.assistant.message.ThreadMessageApi
import com.acv.chat.data.openai.assistant.runs.Run.Status.Cancelled
import com.acv.chat.data.openai.assistant.runs.Run.Status.Cancelling
import com.acv.chat.data.openai.assistant.runs.Run.Status.Completed
import com.acv.chat.data.openai.assistant.runs.Run.Status.Expired
import com.acv.chat.data.openai.assistant.runs.Run.Status.Failed
import com.acv.chat.data.openai.assistant.runs.Run.Status.InProgress
import com.acv.chat.data.openai.assistant.runs.Run.Status.Queued
import com.acv.chat.data.openai.assistant.runs.Run.Status.RequiresAction
import com.acv.chat.data.openai.assistant.thread.ThreadApi
import com.acv.chat.domain.DomainError
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

context(Raise<DomainError>, RunApi, AssistantApi, ThreadApi, ThreadMessageApi, ActionSolver)
tailrec suspend operator fun Run.invoke() {
  when (status) {
    InProgress -> {
      delay(5.seconds)
      getRun(runId, threadId)()
    }
    Queued -> {
      delay(5.seconds)
      getRun(runId, threadId)()
    }
    RequiresAction -> {
      val run = submitToolOutputs(
        runId = runId,
        treadId = threadId,
        outputs = solveActions()
      )
      getRun(run.runId, run.threadId)()
    }
    Cancelled,
    Cancelling,
    Completed,
    Expired,
    Failed -> {
    }
  }
}