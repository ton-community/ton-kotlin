package org.ton.experimental

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ton.proxy.ProxyClient

suspend fun main() {
    val file = HttpClient(CIO).get("https://ton.org/global-config.json").readBytes().decodeToString()
    val proxy =
        ProxyClient(
            Json {
                ignoreUnknownKeys = true
            }.decodeFromString(file)
        ).start(true)
}
