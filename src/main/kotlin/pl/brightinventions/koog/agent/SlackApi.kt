package pl.brightinventions.koog.agent

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface SlackApi {
    suspend fun sendMessage(channel: String, text: String): Boolean
}

class SlackApiClient(
    private val httpClient: HttpClient,
    private val properties: SlackProperties,
) : SlackApi {

    override suspend fun sendMessage(channel: String, text: String): Boolean {
        val message = SlackMessage(channel, text)

        val response = httpClient.post {
            url {
                protocol = URLProtocol.HTTPS
                host = "slack.com"
                path("api", "chat.postMessage")
            }

            header(HttpHeaders.Authorization, "Bearer ${properties.token}")
            header(HttpHeaders.ContentType, ContentType.Application.Json)

            setBody(Json.encodeToString(message))
        }
            .call
            .response

        return response.status.value == 200
    }
}

@Serializable
data class SlackMessage(val channel: String, val text: String)

data class SlackProperties(
    val token: String,
)