package org.ton.connect

import io.github.andreypfau.curve25519.x25519.X25519
import io.ktor.client.*
import io.ktor.client.plugins.sse.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.random.Random

public class TonConnectSession(
    private val privateKey: ByteArray
) {
    public constructor() : this(Random.Default.nextBytes(32))

    private val publicKey: ByteArray = X25519.x25519(privateKey)

    public fun getUniversalLink(request: ConnectRequest): String {
        val url = buildString {
            append("v=2")
            append("&id=").append(publicKey.toHexString())
            append("&r=").append(Json.encodeToString(request).encodeURLParameter())
            append("&ret=none")
        }
        return "tc://?$url"
    }

    public suspend fun connect(
        bridgeUrl: String = "https://bridge.tonapi.io/bridge",
        lastEventId: Long = 0
    ) {
        val httpClient = HttpClient {
            install(SSE)
        }
        val url = buildString {
            append(bridgeUrl)
            append("/events?")
            append("client_id=").append(publicKey.toHexString())
            if (lastEventId != 0L) {
                append("&last_event_id=").append(lastEventId)
            }
        }

        httpClient.sse(url) {
            incoming.collect {
                val data = it.data ?: return@collect
                val message = Json.decodeFromString<Message>(data)
                println(message)
                val from = message.from.hexToByteArray()
                val encryptedMessage = Base64.Default.decode(message.message)
                val sharedKey = X25519.x25519(privateKey, from)

            }
        }
    }
}
