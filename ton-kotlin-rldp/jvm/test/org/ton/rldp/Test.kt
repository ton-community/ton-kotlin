package org.ton.rldp

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import org.ton.adnl.engine.CIOAdnlNetworkEngine
import org.ton.adnl.resolver.MapAdnlAddressResolver

fun a() {
    val rldp = Rldp(CIOAdnlNetworkEngine(), MapAdnlAddressResolver(emptyMap()))

    embeddedServer(CIO) {
        routing {
            get {
                call
            }
        }
    }
}
