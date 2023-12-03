package com.acv.chat.data.openai.assistant.runs

import arrow.core.raise.Raise
import arrow.core.raise.either
import com.acv.chat.data.openai.assistant.FunctionWithAction
import com.acv.chat.data.openai.assistant.Tools
import com.acv.chat.data.openai.assistant.functionWithAction
import com.acv.chat.domain.DomainError

interface ActionSolver {
  val scope: ActionScope

  val tools get() = Tools(scope.actions.map { it.tool } + scope.tools)

  suspend operator fun List<ToolCall>.invoke(): List<ToolOutput> =
    map { tool ->
      scope.actions.first { call ->
        call.tool.function?.name == tool.function.name
      }.let {
        it.action(tool.id, tool.function.arguments)
      }
    }
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

fun ActionScope.add(
  tool: AssistantTool
) {
  tools.add(tool)
}

fun actionSolver(block: ActionScope.() -> Unit): ActionSolver =
  object : ActionSolver {
    override val scope: ActionScope
      get() {
        val scope = ActionScope()
        scope.block()
        return scope
      }
  }

typealias Action = suspend (String, String) -> ToolOutput
