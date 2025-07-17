package pl.brightinventions.koog

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

@OptIn(DelicateCoroutinesApi::class)
fun main() {

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                }
            )
        }
    }
    val agent = AIAgent(
        executor = simpleOpenAIExecutor(System.getenv("OPENAI_API_KEY")),
        systemPrompt = """
            You are a developer assistant. When asked about a Jira ticket, fetch its status using the Jira tool.
            When asked to update Slack, use the Slack tool. Compose clear, helpful updates.
    """.trimIndent(),
        llmModel = OpenAIModels.Chat.GPT4o,
        toolRegistry = ToolRegistry {
            tool(
                JiraTool(
                    JiraApiClient(
                        httpClient = httpClient,
                        properties = JiraApiProperties(
                            token = System.getenv("JIRA_TOKEN")
                        )
                    )
                )
            )

            tool(
                SlackTool(
                    slackApi = SlackApiClient(
                        httpClient = httpClient,
                        properties = SlackProperties(
                            token = System.getenv("SLACK_TOKEN")
                        )
                    )
                )
            )
        },
    )

    val command = "Update the team: What’s the status of BA-1 ticket? Post it to #koog-test in Slack."

    runBlocking {
        agent.run(command)
    }
}