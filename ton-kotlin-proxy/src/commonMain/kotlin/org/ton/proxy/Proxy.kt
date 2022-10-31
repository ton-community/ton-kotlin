package org.ton.proxy

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient
import org.ton.proxy.dns.DnsResolver

class Proxy(
    liteClient: LiteClient = LiteClient(
        LiteServerDesc(
            id = PublicKeyEd25519(base64("n4VDnSCUuSpjnCyUk9e3QOOd6o0ItSWYbTnW3Wnn8wk=")),
            ip = 84478511,
            port = 19949
        )
    )
) {
    val dnsResolver = DnsResolver(liteClient)

    val app = embeddedServer(CIO, port = 8080) {
        intercept(ApplicationCallPipeline.Call) {
            val request = call.request
            val path = request.path()

            val domain = if (path.startsWith(HTTP_PROTOCOL)) {
                path.substring(HTTP_PROTOCOL.length).substringBefore('/')
            } else {
                path.substring(HTTPS_PROTOCOL.length).substringBefore('/')
            }
            if (domain.endsWith(".adnl") || domain.endsWith(".ton")) {
                call.respondText(dnsResolver.resolveAll(domain).map { "${it.key} - ${it.value}" }.joinToString("\n"))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Only .adnl and .ton domains are supported")
            }
        }
    }

    fun start(wait: Boolean = true) {
        app.start(wait)
    }

    companion object {
        @Suppress("HttpUrlsUsage")
        private const val HTTP_PROTOCOL = "http://"
        private const val HTTPS_PROTOCOL = "https://"
    }
}

/*
suspend fun main() {
    val address = AdnlAddressUdp(
        ip = -1307380867,
        port = 15888
    )
    val key = PublicKeyEd25519(
        base64("C1uy64rfGxp10SPSqbsxWhbumy5SM0YbvljCudwpZeI=")
    )
    val peer = Peer(
        address = address,
        key = key
    )
    peer.sendPacket(
        null,
        AdnlMessageQuery(
            query_id = ByteArray(32),
            query = DhtPing(0).toByteArray()
        )
    )
    while (true) {

    }
 */
