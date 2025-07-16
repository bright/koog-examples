package pl.brightinventions.koog

import ai.koog.agents.core.tools.SimpleTool
import ai.koog.agents.core.tools.ToolDescriptor
import ai.koog.agents.core.tools.ToolParameterDescriptor
import ai.koog.agents.core.tools.ToolParameterType
import ai.koog.agents.ext.tool.AskUser
import kotlinx.serialization.KSerializer

class JiraTool(val jiraApi: JiraApi) : SimpleTool<AskUser.Args>() {

    override suspend fun doExecute(args: AskUser.Args): String {
        return jiraApi.getTicketStatus(args.message.extractJiraTicketKey())
            .let {
                "The status of the Jira ticket ${args.message.extractJiraTicketKey()} is: $it"
            }
    }

    override val argsSerializer: KSerializer<AskUser.Args> = AskUser.Args.serializer()
    override val descriptor: ToolDescriptor = ToolDescriptor(
        name = "__get_jira_ticket_status__",
        description = "Service tool, used by the agent to get jira ticket status",
        requiredParameters = listOf(
            ToolParameterDescriptor(
                name = "message", description = "Message from the agent", type = ToolParameterType.String
            )
        )
    )
}

private fun String.extractJiraTicketKey(): String {
    val regex = Regex("""BA-\d+""")
    return regex.find(this)?.value ?: error("Invalid jira ticket key: $this")
}
