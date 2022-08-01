package org.ton.adnl.client

import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.ktor.utils.io.CancellationException
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.ton.crypto.aes.AesCtr
import kotlin.coroutines.CoroutineContext

class AdnlTcpSession(
    private val input: ByteReadChannel,
    private val output: ByteWriteChannel,
    coroutineContext: CoroutineContext
) : CoroutineScope {
    private val socketJob: CompletableJob = Job(coroutineContext[Job])

    private val _incoming = Channel<AdnlPacket>(capacity = 8)
    private val _outgoing = Channel<Any>(capacity = 8)

    override val coroutineContext = coroutineContext + socketJob + CoroutineName("adnl")

    val incoming: ReceiveChannel<AdnlPacket> get() = _incoming
    val outgoing: SendChannel<AdnlPacket> get() = _outgoing

    @Volatile
    private lateinit var nonce: ByteArray

    private val incomingCipher by lazy {
        AesCtr(nonce.copyOfRange(0, 32), nonce.copyOfRange(64, 80))
    }
    private val outgoingCipher by lazy {
        AesCtr(nonce.copyOfRange(32, 64), nonce.copyOfRange(80, 96))
    }

    private val writerJob = launch(context = CoroutineName("adnl-writer"), start = CoroutineStart.LAZY) {
        try {
            mainLoop@ while (isActive) when (val packet = _outgoing.receive()) {
                is AdnlPacket -> {
                    output.writeAdnlPacket(outgoingCipher, packet)
                }

                is FlushRequest -> {
                    output.flush()
                    packet.complete()
                }

                else -> throw IllegalStateException("Unknown packet $packet")
            }
        } catch (cause: ChannelWriteException) {
            _outgoing.close(CancellationException("Failed to write to ADNL", cause))
        } catch (t: Throwable) {
            _outgoing.close(t)
        } finally {
            _outgoing.close(CancellationException("ADNL closed"))
            output.close()
        }

        while (true) when (val message = _outgoing.tryReceive().getOrNull() ?: break) {
            is FlushRequest -> message.complete()
            else -> {}
        }
    }

    private val readerJob = launch(CoroutineName("adnl-reader"), start = CoroutineStart.LAZY) {
        try {
            while (isActive) {
                val packet = input.readAdnlPacket(incomingCipher)
                _incoming.send(packet)
            }
        } catch (cause: CancellationException) {
            _incoming.cancel(cause)
        } catch (eof: EOFException) {
            // no more bytes is possible to read
        } catch (eof: ClosedReceiveChannelException) {
            // no more bytes is possible to read
        } catch (io: ChannelIOException) {
            _incoming.cancel()
        } catch (cause: Throwable) {
            _incoming.close(cause)
            throw cause
        } finally {
            _incoming.close()
        }
    }

    suspend fun send(adnlPacket: AdnlPacket) {
        outgoing.send(adnlPacket)
    }

    suspend fun flush(): Unit = FlushRequest(coroutineContext[Job]).also {
        try {
            _outgoing.send(it)
        } catch (closed: ClosedSendChannelException) {
            it.complete()
            writerJob.join()
        } catch (sendFailure: Throwable) {
            it.complete()
            throw sendFailure
        }
    }.await()

    private class FlushRequest(parent: Job?) {
        private val done: CompletableJob = Job(parent)
        fun complete(): Boolean = done.complete()
        suspend fun await(): Unit = done.join()
    }
}
