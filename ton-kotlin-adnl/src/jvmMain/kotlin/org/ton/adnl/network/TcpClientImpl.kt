package org.ton.adnl.network

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineName
import kotlin.coroutines.CoroutineContext

internal actual class TcpClientImpl actual constructor(
    coroutineContext: CoroutineContext
) : TcpClient {
    override val coroutineContext: CoroutineContext = coroutineContext + CoroutineName("TcpClient")

    private lateinit var socket: Socket
    private lateinit var connection: Connection

    override suspend fun connect(host: String, port: Int) {
        socket = aSocket(SelectorManager(coroutineContext)).tcp().connect(host, port)
        connection = socket.connection()
    }

    override suspend fun send(data: ByteReadPacket) {
        connection.output.writePacket(data)
    }

    override suspend fun receive(size: Int): ByteReadPacket {
        return connection.input.readPacket(size)
    }
}
