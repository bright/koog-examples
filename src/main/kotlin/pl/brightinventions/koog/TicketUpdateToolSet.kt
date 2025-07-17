package pl.brightinventions.koog

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet

@LLMDescription("A set of tools for updating Jira tickets and sending messages to Slack.")
class TicketUpdateToolSet(
    private val jiraApi: JiraApi,
    private val slackApi: SlackApi,
): ToolSet {

    @Tool
    @LLMDescription("Retrieves the status of a Jira ticket by its key.")
    suspend fun jiraTool(
        @LLMDescription("The key of the Jira ticket, e.g. BA-1")
        ticketKey: String,
    ): String {
        return jiraApi.getTicketStatus(ticketKey).let {
            "The status of the Jira ticket $ticketKey is: $it"
        }
    }

    @Tool
    @LLMDescription("Sends a message to a Slack channel.")
    suspend fun slackTool(
        @LLMDescription("The name of the Slack channel where the message should be sent")
        channel: String,
        @LLMDescription("The text of the message to be sent")
        text: String,
    ): String {
        val isMessageSent = slackApi.sendMessage(channel, text)
        return if (isMessageSent) "Message was sent." else "Message could not be sent."
    }
}