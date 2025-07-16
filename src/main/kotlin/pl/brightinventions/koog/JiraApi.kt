package pl.brightinventions.koog

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

interface JiraApi {
    suspend fun getTicketStatus(ticketKey: String): String
}

class JiraApiClient(
    private val httpClient: HttpClient,
    private val properties: JiraApiProperties
) : JiraApi {

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun getTicketStatus(ticketKey: String): String {
        return httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "bright-inventions.atlassian.net"
                path("/rest/api/2/issue/$ticketKey")
            }

            header(HttpHeaders.Authorization, Base64.encode("${properties.email}:${properties.token}".toByteArray()))
        }
            .call
            .body<JiraIssue>()
            .fields.status.name
    }
}

@Serializable
data class JiraIssue(
    val key: String,
    val fields: Fields
) {
    @Serializable
    data class Fields(
        val status: Status
    ) {
        @Serializable
        data class Status(
            val name: String
        )
    }
}

data class JiraApiProperties(
    val email: String,
    val token: String,
)