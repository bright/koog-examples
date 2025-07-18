package pl.brightinventions.koog.financial.news.monitor

import ai.koog.prompt.markdown.markdown
import ai.koog.prompt.structure.markdown.MarkdownStructuredDataDefinition

fun markdownArticleDefinition(): MarkdownStructuredDataDefinition {
    return MarkdownStructuredDataDefinition("articlesList", schema = {
        markdown {
            header(1, "articleTitle")
            bulleted {
                item("summary")
                item("author")
                item("source")
                item("url")
            }
        }
    }, examples = {
        markdown {
            header(1, "CocaCola stock is reaching all time high!")
            bulleted {
                item("CocaCola's stock has reached an all-time high, driven by strong quarterly earnings and increased consumer demand.")
                item("John Doe")
                item("Financial News Daily")
                item("https://financialnewsdaily.com/coca-cola-stock-high")
            }
        }
    })
}