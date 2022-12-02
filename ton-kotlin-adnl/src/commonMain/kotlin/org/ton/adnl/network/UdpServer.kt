package org.ton.adnl.network

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface UdpServer : CoroutineScope {
    suspend fun send(host: String, port: Int, data: ByteReadPacket)

    fun interface Callback {
        suspend fun receive(host: String, port: Int, data: ByteReadPacket)
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
