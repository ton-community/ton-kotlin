package org.ton.adnl.client.socket

import io.ktor.network.sockets.*
import io.ktor.network.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.pool.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.withContext
import org.ton.adnl.client.AdnlClientHandshake
import org.ton.adnl.client.AdnlConfig
import org.ton.adnl.client.AdnlConfigBuilder
import org.ton.adnl.client.AdnlPacket
import kotlin.coroutines.CoroutineContext

suspend fun Socket.adnl(
    coroutineContext: CoroutineContext,
    block: AdnlConfigBuilder.() -> Unit
): Socket = adnl(coroutineContext, AdnlConfigBuilder().apply(block).build())

suspend fun Socket.adnl(
    context: CoroutineContext,
    config: AdnlConfig
): Socket {
    val reader = openReadChannel()
    val writer = openWriteChannel()
    return try {
        openAdnlSession(this, reader, writer, config, context)
    } catch (cause: Throwable) {
        reader.cancel(cause)
        writer.close(cause)
        withContext(Dispatchers.IO) {
            close()
        }
        throw cause
    }
}

fun openAdnlSession(
    socket: Socket,
    input: ByteReadChannel,
    output: ByteWriteChannel,
    config: AdnlConfig,
    context: CoroutineContext
): Socket {
    val handshake = try {
        AdnlClientHandshake(input, output, config, context)
    } catch (cause: ClosedSendChannelException) {
        throw IllegalStateException("ADNL Handshake failed. Invalid server public key?", cause)
    }
    return AdnlTcpSocket(handshake.input, handshake.output, socket, context)
}

private class AdnlTcpSocket(
    private val input: ReceiveChannel<AdnlPacket>,
    private val output: SendChannel<AdnlPacket>,
    private val socket: Socket,
    override val coroutineContext: CoroutineContext
) : CoroutineScope, Socket by socket {

    override fun attachForReading(channel: ByteChannel): WriterJob =
        writer(coroutineContext + CoroutineName("adnl-input-loop"), channel) {
            dataInputLoop(this.channel)
        }

    override fun attachForWriting(channel: ByteChannel): ReaderJob =
        reader(coroutineContext + CoroutineName("adnl-output-loop"), channel) {
            dataOutputLoop(this.channel)
        }

    private suspend fun dataInputLoop(pipe: ByteWriteChannel) {
        try {
            input.consumeEach { packet ->
                val payload = packet.payload
                pipe.writePacket(payload)
                pipe.flush()
            }
        } catch (_: Throwable) {
        } finally {
            pipe.close()
        }
    }

    private suspend fun dataOutputLoop(pipe: ByteReadChannel) = DefaultByteBufferPool.useInstance { buffer ->
        try {
            while (true) {
                buffer.clear()
                val rc = pipe.readAvailable(buffer)
                if (rc == -1) break
                buffer.flip()
                output.send(
                    AdnlPacket {
                        writeFully(buffer)
                    }
                )
            }
        } finally {
            output.close()
        }
    }

    override fun dispose() {
        socket.dispose()
    }
}
