package org.ton.adnl.network

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.io.Source
import kotlin.coroutines.CoroutineContext

internal actual class UdpServerImpl actual constructor(
    coroutineContext: CoroutineContext,
    actual val port: Int,
    callback: UdpServer.Callback
) : UdpServer {
    actual override val coroutineContext: CoroutineContext = coroutineContext + CoroutineName(toString())
    private val deferredSocket = async {
        aSocket(SelectorManager(coroutineContext + CoroutineName("selector-$port")))
            .udp()
            .bind(localAddress = InetSocketAddress("0.0.0.0", port))
    }

    private val job = launch(CoroutineName("listener-$port")) {
        val socket = deferredSocket.await()
        while (true) {
            val datagram = socket.receive()
            val socketAddress = datagram.address as InetSocketAddress
            val address = socketAddress.hostname
            val port = socketAddress.port
            val data = datagram.packet
            try {
                launch {
                    callback.receive(IPAddress.ipv4(address, port), data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    actual override suspend fun send(address: IPAddress, data: Source) {
        val socket = deferredSocket.await()
        val datagram = Datagram(data, InetSocketAddress(address.host, port))
        socket.send(datagram)
    }
}
