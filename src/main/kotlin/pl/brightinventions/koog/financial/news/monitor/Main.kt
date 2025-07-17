package pl.brightinventions.koog.financial.news.monitor

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import kotlinx.coroutines.runBlocking

fun main() {
    val agentStrategy = strategy("articles-assistant") {
        val getMdOutput by node<String, String> { input ->
            val mdDefinition = markdownArticleDefinition()

            llm.writeSession {
                updatePrompt { user(input) }
                val markdownStream = requestLLMStreaming(mdDefinition)

                parseMarkdownStreamToArticles(markdownStream)
                    .collect { article ->
                        callTool<ArticleTool>(article)
                    }
            }

            ""
        }

        edge(nodeStart forwardTo getMdOutput)
        edge(getMdOutput forwardTo nodeFinish)
    }


    val agent = AIAgent(
        promptExecutor = simpleOpenAIExecutor(System.getenv("OPENAI_API_KEY")),
        strategy = agentStrategy,
        agentConfig = AIAgentConfig.withSystemPrompt(
            prompt = """
            You're AI financial news assistant. Please provide users with comprehensive and structured information about the financial news of the world.
        """.trimIndent()
        ),
        toolRegistry = ToolRegistry {
            tool(ArticleTool())
        },
    )

    val command = """
        Provide me latest financial news.
    """.trimIndent()

    runBlocking { agent.run(command) }
}