package org.ton.adnl.network

import io.ktor.utils.io.core.*
import kotlin.coroutines.CoroutineContext

internal actual class UdpServerImpl actual constructor(
    coroutineContext: CoroutineContext,
    port: Int,
    callback: UdpServer.Callback
) : UdpServer {
    actual val port: Int
        get() = TODO("Not yet implemented")

    override suspend fun send(address: IPAddress, data: ByteReadPacket) {
        TODO("Not yet implemented")
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")
}
