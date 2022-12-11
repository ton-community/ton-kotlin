package org.ton.adnl.network

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface UdpServer : CoroutineScope {
    suspend fun send(address: IPAddress, data: ByteReadPacket)

    fun interface Callback {
        fun receive(address: IPAddress, data: ByteReadPacket)
    }

    companion object {
        fun create(
            coroutineContext: CoroutineContext,
            port: Int,
            callback: Callback
        ): UdpServer {
            return UdpServerImpl(coroutineContext, port, callback)
        }
    }
}
