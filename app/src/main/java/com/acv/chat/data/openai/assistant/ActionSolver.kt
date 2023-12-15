package com.acv.chat.data.openai.assistant

import arrow.core.raise.Raise
import arrow.core.raise.either
import com.acv.chat.data.openai.assistant.runs.ToolCall
import com.acv.chat.data.openai.assistant.runs.ToolOutput
import com.acv.chat.domain.DomainError

interface ActionSolver {
  val scope: ActionScope

  val tools get() = Tools(scope.actions.map { it.tool } + scope.tools)

  context(Raise<DomainError>)
  suspend fun solve(toolCalls: List<ToolCall>): List<ToolOutput>
}

class ActionScope {
  val actions = mutableListOf<FunctionWithAction>()
  val tools = mutableListOf<AssistantTool>()
}

inline fun <reified A> ActionScope.add(
  name: String? = null,
  description: String? = null,
  crossinline block: suspend Raise<DomainError>.(A) -> String?
) {
  either {
    functionWithAction<A>(name = name, description = description, block = { block(it) })
  }.fold(
    { println(it) },
    { actions.add(it) }
  )
}

fun ActionScope.addRetrievalTool() {
  tools.add(AssistantTool.RetrievalTool)
}

fun ActionScope.addCodeInterpreter() {
  tools.add(AssistantTool.CodeInterpreter)
}

fun actionSolver(block: ActionScope.() -> Unit): ActionSolver =
  object : ActionSolver {
    override val scope: ActionScope
      get() {
        val scope = ActionScope()
        scope.block()
        return scope
      }

    context(Raise<DomainError>) override suspend fun solve(toolCalls: List<ToolCall>): List<ToolOutput> =
      toolCalls.map { tool ->
        scope.actions
          .firstOrNull { it.tool.function.name == tool.function.name }
          ?.let { it.action(tool.id, tool.function.arguments) }
          ?: raise(DomainError.UnknownDomainError("No action found for ${tool.function.name}"))
      }
  }

typealias Action = suspend (String, String) -> ToolOutput
