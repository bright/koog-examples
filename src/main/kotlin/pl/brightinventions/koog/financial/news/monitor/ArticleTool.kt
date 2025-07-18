package pl.brightinventions.koog.financial.news.monitor

import ai.koog.agents.core.tools.SimpleTool
import ai.koog.agents.core.tools.ToolDescriptor
import kotlinx.serialization.KSerializer

class ArticleTool : SimpleTool<Article>() {

    override suspend fun doExecute(args: Article): String {
        return args.toString()
    }

    override val argsSerializer: KSerializer<Article> = Article.serializer()

    override val descriptor: ToolDescriptor = ToolDescriptor(
        name = "articleTool",
        description = "A tool to parse book information from markdown",
        requiredParameters = listOf(),
        optionalParameters = listOf()
    )
}