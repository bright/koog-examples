package pl.brightinventions.koog

import ai.koog.agents.core.tools.SimpleTool
import ai.koog.agents.core.tools.ToolDescriptor
import ai.koog.agents.core.tools.ToolParameterDescriptor
import ai.koog.agents.core.tools.ToolParameterType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

class JiraTool(val jiraApi: JiraApi) : SimpleTool<JiraTool.JiraToolArgs>() {

    @Serializable
    data class JiraToolArgs(val ticketKey: String) : Args

    override suspend fun doExecute(args: JiraToolArgs): String {
        return jiraApi.getTicketStatus(args.ticketKey).let {
            "The status of the Jira ticket ${args.ticketKey} is: $it"
        }
    }

    override val argsSerializer: KSerializer<JiraToolArgs> = JiraToolArgs.serializer()
    override val descriptor: ToolDescriptor = ToolDescriptor(
        name = "__get_jira_ticket_status__",
        description = "Service tool, used by the agent to get jira ticket status",
        requiredParameters = listOf(
            ToolParameterDescriptor(
                name = "ticketKey",
                description = "Ticket key in Jira, e.g. BA-1",
                type = ToolParameterType.String
            )
        )
    )
}
