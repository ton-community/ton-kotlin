@file:Suppress("OPT_IN_USAGE")

package org.ton.adnl.client

import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.*
import org.ton.adnl.packet.AdnlHandshakePacket
import org.ton.crypto.aes.AesCtr
import kotlin.coroutines.CoroutineContext

class AdnlClientHandshake(
    rawInput: ByteReadChannel,
    rawOutput: ByteWriteChannel,
    private val config: AdnlSocketConfig,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {
    private val authNonce = config.random.nextBytes(160)
    private val inputCipher = AesCtr(authNonce.copyOfRange(0, 32), authNonce.copyOfRange(64, 80))
    private val outputCipher = AesCtr(authNonce.copyOfRange(32, 64), authNonce.copyOfRange(80, 96))

    val input: ReceiveChannel<AdnlPacket> = produce(CoroutineName("adnl-input-decoder")) {
        var performedHandshake = false
        try {
            loop@ while (true) {
                if (performedHandshake) {
                    val packet = rawInput.readAdnlPacket(inputCipher)
                    println("Read packet: $packet")
                    channel.send(packet)
                } else {
                    val packet = rawInput.readAdnlPacket(inputCipher)
                    check(packet.data.remaining == 0L)
                    println("all done!")
                    performedHandshake = true
                }
            }
        } catch (cause: ClosedReceiveChannelException) {
            channel.close()
        } catch (cause: Throwable) {
            channel.close(cause)
        } finally {
            output.close()
        }
    }

    val output: SendChannel<AdnlPacket> = actor(CoroutineName("adnl-output-encoder")) {
        var performedHandshake = false
        try {
            channel.consumeEach { rawPacket ->
                try {
                    if (performedHandshake) {
                        rawOutput.writeAdnlPacket(outputCipher, rawPacket)
                    } else {
                        val handshake = AdnlHandshakePacket(authNonce, config.serverPublicKey)
                        rawOutput.writePacket(handshake.build())
                        rawOutput.flush()
                        performedHandshake = true
                        println("Performed handshake: $handshake")
                    }
                } catch (cause: Throwable) {
                    channel.close(cause)
                }
            }
        } finally {
            rawOutput.close()
        }
    }
}
