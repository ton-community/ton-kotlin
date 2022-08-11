package org.ton.adnl.client.socket

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.sync.Semaphore

internal class ConnectionFactory(
    private val selector: SelectorManager,
    maxConnectionsCount: Int
) {
    private val semaphore = Semaphore(maxConnectionsCount)

    suspend fun connect(
        address: InetSocketAddress,
        configuration: SocketOptions.TCPClientSocketOptions.() -> Unit = {}
    ): Socket {
        semaphore.acquire()
        return try {
            aSocket(selector).tcpNoDelay().tcp().connect(address)
        } catch (cause: Throwable) {
            // a failure or cancellation
            semaphore.release()
            throw cause
        }
    }

    fun release() {
        semaphore.release()
    }
}
