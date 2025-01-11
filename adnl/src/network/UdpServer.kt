package org.ton.adnl.network

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.io.Source
import kotlin.coroutines.CoroutineContext

public interface UdpServer : CoroutineScope {
    public suspend fun send(address: IPAddress, data: Source)

    public fun interface Callback {
        public fun receive(address: IPAddress, data: Source)
    }

    public companion object {
        public fun create(
            coroutineContext: CoroutineContext,
            port: Int,
            callback: Callback
        ): UdpServer {
            return UdpServerImpl(coroutineContext, port, callback)
        }
    }
}
