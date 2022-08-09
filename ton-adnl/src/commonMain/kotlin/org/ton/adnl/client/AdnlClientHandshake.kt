@file:Suppress("OPT_IN_USAGE")

package org.ton.adnl.client

import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.*
import org.ton.adnl.client.socket.cipher
import org.ton.adnl.packet.AdnlHandshakePacket
import kotlin.coroutines.CoroutineContext

class AdnlClientHandshake(
    rawInput: ByteReadChannel,
    rawOutput: ByteWriteChannel,
    private val config: AdnlConfig,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {
    //    private val authNonce = config.random.nextBytes(160)
    private val authNonce = ByteArray(160)
    private val inputCipher = cipher(authNonce.copyOfRange(0, 32), authNonce.copyOfRange(64, 80))
    private val outputCipher = cipher(authNonce.copyOfRange(32, 64), authNonce.copyOfRange(80, 96))

    val input: ReceiveChannel<AdnlPacket> = produce(CoroutineName("adnl-input-decoder")) {
        var performedHandshake = false
        try {
            val cipherInput = rawInput.cipher(coroutineContext, inputCipher)
            loop@ while (true) {
                if (performedHandshake) {
                    val packet = cipherInput.readAdnlPacket()
                    channel.send(packet)
                } else {
                    val packet = cipherInput.readAdnlPacket()
                    check(packet.payload.remaining == 0L)
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
            val handshake = AdnlHandshakePacket(authNonce, config.serverPublicKey)
            val packet = handshake.build()
            rawOutput.writePacket(packet)
            rawOutput.flush()
            val cipherOutput = rawOutput.cipher(coroutineContext, outputCipher)
            channel.consumeEach { rawPacket ->
                try {
                    cipherOutput.writeAdnlPacket(rawPacket)
                    cipherOutput.flush()
                } catch (cause: Throwable) {
                    cause.printStackTrace()
                    channel.close(cause)
                }
            }
        } finally {
            rawOutput.close()
        }
    }
}
