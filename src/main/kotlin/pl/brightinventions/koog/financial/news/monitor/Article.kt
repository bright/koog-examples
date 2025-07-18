package pl.brightinventions.koog.financial.news.monitor

import ai.koog.agents.core.tools.Tool.Args
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val title: String,
    val summary: String,
    val author: String,
    val source: String,
    val url: String,
) : Args {

    override fun toString(): String {
        return "$title by $author, source: $source, URL: $url\nSummary: $summary"
    }
}
