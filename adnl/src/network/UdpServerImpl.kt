package org.ton.adnl.network

import kotlinx.io.Source
import kotlin.coroutines.CoroutineContext

internal expect class UdpServerImpl(
    coroutineContext: CoroutineContext,
    port: Int,
    callback: UdpServer.Callback
) : UdpServer {
    val port: Int
    override suspend fun send(address: IPAddress, data: Source)
    override val coroutineContext: CoroutineContext
}
