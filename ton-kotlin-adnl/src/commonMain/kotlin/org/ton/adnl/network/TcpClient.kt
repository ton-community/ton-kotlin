package org.ton.adnl.network

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DisposableHandle

public interface TcpClient : Closeable, DisposableHandle {
    val input: ByteReadChannel
    val output: ByteWriteChannel

    public suspend fun connect(host: String, port: Int)

    override fun dispose() {
        try {
            close()
        } catch (ignore: Throwable) {}
    }

    fun close(cause: Throwable?)
}
