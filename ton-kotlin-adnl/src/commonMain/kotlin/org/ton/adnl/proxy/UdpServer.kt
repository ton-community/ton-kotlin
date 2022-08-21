package org.ton.adnl.proxy

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UdpServer(
    port: Int,
    val callback: suspend (Datagram) -> Unit,
) {
    val socket = aSocket(SelectorManager()).udp().bind(
        InetSocketAddress("0.0.0.0", port)
    )
    val job = GlobalScope.launch {
        while (true) {
            val datagram = socket.receive()
            callback(datagram)
        }
    }

    suspend fun send(datagram: Datagram) {
        socket.send(datagram)
    }
}
