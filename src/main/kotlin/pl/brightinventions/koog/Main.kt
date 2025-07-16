package pl.brightinventions.koog

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.ext.tool.AskUser
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import io.ktor.client.HttpClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun main() {

    val agent = AIAgent(
        executor = simpleOpenAIExecutor(System.getenv("OPENAI_API_KEY")),
        systemPrompt = """
        You are a developer assistant. Use AskUser tool to get Jira ticket number from the user.
        When Jira ticket number was provided, fetch its status using the Jira tool.
        When asked to update Slack, use the Slack tool. Compose clear, helpful updates.
    """.trimIndent(),
        llmModel = OpenAIModels.Chat.GPT4o,
        toolRegistry = ToolRegistry {
            tool(
                JiraTool(
                    JiraApiClient(
                        httpClient = HttpClient(),
                        properties = JiraApiProperties(
                            email = System.getenv("JIRA_EMAIL"),
                            token = System.getenv("JIRA_TOKEN")
                        )
                    )
                )
            )
            tool(AskUser)
        },
    )

    val command = "Update the team: What’s the status of BA-1 ticket? Post it to #dev-updates in Slack."

    GlobalScope.launch {
        val result = agent.run(command)
        println(result)
    }
}