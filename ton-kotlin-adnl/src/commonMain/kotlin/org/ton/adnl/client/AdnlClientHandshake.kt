@file:Suppress("OPT_IN_USAGE")

package org.ton.adnl.client

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.*
import org.ton.adnl.packet.AdnlHandshakePacket
import org.ton.crypto.aes.AesCtr
import kotlin.coroutines.CoroutineContext

class AdnlClientHandshake(
    rawInput: ByteReadChannel,
    rawOutput: ByteWriteChannel,
    private val config: AdnlConfig,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {
    private val authNonce = config.random.nextBytes(160)
    private val inputCipher = AesCtr(authNonce.copyOfRange(0, 32), authNonce.copyOfRange(64, 80))
    private val outputCipher = AesCtr(authNonce.copyOfRange(32, 64), authNonce.copyOfRange(80, 96))

    private val encryptedInput by lazy {
        writer(CoroutineName("adnl-input-encrypt"), autoFlush = true) {
            inputCipher.encrypt(src = rawInput, dst = channel)
        }.channel
    }

    val input: ReceiveChannel<AdnlPacket> = produce(CoroutineName("adnl-input-decoder")) {
        try {
            loop@ while (true) {
                val packet = encryptedInput.readAdnlPacket()
                channel.send(packet)
            }
        } catch (cause: ClosedReceiveChannelException) {
            channel.close()
        } catch (cause: Throwable) {
            channel.close(cause)
        } finally {
            output.close()
        }
    }

    private val encryptedOutput by lazy {
        reader(CoroutineName("adnl-output-encrypt"), autoFlush = true) {
            outputCipher.encrypt(src = channel, dst = rawOutput)
        }.channel
    }

    val output: SendChannel<AdnlPacket> by lazy {
        TODO()
    }

//    val output: SendChannel<AdnlPacket> = actor(CoroutineName("adnl-output-encoder")) {
//        try {
//            val handshake = AdnlHandshakePacket(authNonce, config.serverPublicKey)
//            val packet = handshake.build()
//            rawOutput.writePacket(packet)
//            rawOutput.flush()
//
//            channel.consumeEach { rawPacket ->
//                try {
//                    encryptedOutput.writeAdnlPacket(rawPacket)
//                    encryptedOutput.flush()
//                } catch (cause: Throwable) {
//                    cause.printStackTrace()
//                    channel.close(cause)
//                }
//            }
//        } finally {
//            rawOutput.close()
//        }
//    }

    suspend fun AesCtr.encrypt(src: ByteReadChannel, dst: ByteWriteChannel) {
        val buffer = ChunkBuffer.Pool.borrow()
        try {
            var closed = false
            while (!closed) {
                buffer.reset()
                closed = src.readAvailable(buffer) == -1
                val bytes = if (closed) {
                    doFinal()
                } else {
                    update(buffer.readBytes())
                }
                dst.writeFully(bytes)
                dst.flush()
            }
        } catch (t: Throwable) {
            dst.close(t)
            throw t
        } finally {
            buffer.release(ChunkBuffer.Pool)
        }
    }
}
