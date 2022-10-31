package org.ton.proxy

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.response.*

@Suppress("HttpUrlsUsage")
private const val HTTP_PROTOCOL = "http://"
private const val HTTPS_PROTOCOL = "https://"

fun main() {
    embeddedServer(CIO, port = 8080) {
        intercept(ApplicationCallPipeline.Call) {
            val request = call.request
            val path = request.path()

            val domain = if (path.startsWith(HTTP_PROTOCOL)) {
                path.substring(HTTP_PROTOCOL.length).substringBefore('/')
            } else {
                path.substring(HTTPS_PROTOCOL.length).substringBefore('/')
            }
            if (domain.endsWith(".adnl") || domain.endsWith(".ton")) {
                call.respondText(DNS_RESOLVER.resolveAll(domain).map { "${it.key} - ${it.value}" }.joinToString("\n"))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Only .adnl and .ton domains are supported")
            }
        }
    }.start(wait = true)
}
