package pl.brightinventions.koog

import ai.koog.agents.core.tools.SimpleTool
import ai.koog.agents.core.tools.Tool
import ai.koog.agents.core.tools.ToolDescriptor
import ai.koog.agents.core.tools.ToolResult
import ai.koog.agents.ext.tool.AskUser
import kotlinx.serialization.KSerializer

class SlackTool : SimpleTool<AskUser.Args>() {

    override suspend fun doExecute(args: AskUser.Args): String {
        TODO("Not yet implemented")
    }

    override val argsSerializer: KSerializer<AskUser.Args>
        get() = TODO("Not yet implemented")
    override val descriptor: ToolDescriptor
        get() = TODO("Not yet implemented")
}