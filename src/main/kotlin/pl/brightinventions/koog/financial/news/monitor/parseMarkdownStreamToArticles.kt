package pl.brightinventions.koog.financial.news.monitor

import ai.koog.prompt.structure.markdown.markdownStreamingParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

fun parseMarkdownStreamToArticles(markdownStream: Flow<String>): Flow<Article> {
    return flow {
        markdownStreamingParser {
            var currentArticleTitle = ""
            val bulletPoints = mutableListOf<String>()

            onHeader(1) { headerText ->
                if (currentArticleTitle.isNotEmpty() && bulletPoints.isNotEmpty()) {
                    mapAndEmit(bulletPoints, currentArticleTitle)
                }

                currentArticleTitle = headerText
                bulletPoints.clear()
            }

            onBullet { bulletText ->
                bulletPoints.add(bulletText)
            }

            onFinishStream {
                if (currentArticleTitle.isNotEmpty() && bulletPoints.isNotEmpty()) {
                    mapAndEmit(bulletPoints, currentArticleTitle)
                }
            }
        }.parseStream(markdownStream)
    }
}

private suspend fun FlowCollector<Article>.mapAndEmit(
    bulletPoints: List<String>,
    currentArticleTitle: String
) {
    val summary = bulletPoints.getOrNull(0) ?: ""
    val author = bulletPoints.getOrNull(1) ?: ""
    val source = bulletPoints.getOrNull(2) ?: ""
    val url = bulletPoints.getOrNull(3) ?: ""

    emit(
        Article(
            title = currentArticleTitle,
            summary = summary,
            author = author,
            source = source,
            url = url
        )
    )
}