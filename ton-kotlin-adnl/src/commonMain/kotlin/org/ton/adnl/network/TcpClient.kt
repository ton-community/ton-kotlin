package org.ton.adnl.network

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope

interface TcpClient : CoroutineScope {
    suspend fun connect(host: String, port: Int)

    suspend fun send(data: ByteReadPacket)

    suspend fun receive(size: Int): ByteReadPacket
}
