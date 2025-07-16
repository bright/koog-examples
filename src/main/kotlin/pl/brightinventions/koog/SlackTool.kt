package pl.brightinventions.koog

import ai.koog.agents.core.tools.SimpleTool
import ai.koog.agents.core.tools.ToolDescriptor
import ai.koog.agents.core.tools.ToolParameterDescriptor
import ai.koog.agents.core.tools.ToolParameterType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

class SlackTool(
    private val slackApi: SlackApi,
) : SimpleTool<SlackTool.SlackToolArgs>() {

    @Serializable
    data class SlackToolArgs(val channel: String, val text: String) : Args

    override suspend fun doExecute(args: SlackToolArgs): String {
        val isMessageSent = slackApi.sendMessage(
            channel = args.channel,
            text = args.text,
        )

        return if (isMessageSent) "Message was sent." else "Message could not be sent."
    }

    override val argsSerializer: KSerializer<SlackToolArgs> = SlackToolArgs.serializer()

    override val descriptor: ToolDescriptor = ToolDescriptor(
        name = "__send_slack_message__",
        description = "Service tool, used by the agent to send a message to Slack",
        requiredParameters = listOf(
            ToolParameterDescriptor(
                name = "channel",
                description = "Name of the channel, where message should be sent",
                type = ToolParameterType.String
            ),
            ToolParameterDescriptor(
                name = "text",
                description = "Text of the message to be sent",
                type = ToolParameterType.String
            )
        )
    )
}