package org.ton.adnl.aes

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.produce
import kotlin.coroutines.CoroutineContext

class AdnlRecord(
    val packet: ByteReadPacket = ByteReadPacket.Empty
)

class AdnlAesSocket(
    rawInput: ByteReadChannel,
    rawOutput: ByteWriteChannel,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {
    val input = produce<ByteReadPacket>(CoroutineName("adnl-aes-parser")) {
        var useCipher = false
        try {

        } catch (cause: ClosedReceiveChannelException) {
            channel.close()
        } catch (cause: Throwable) {
            channel.close(cause)
        } finally {
            output.close()
        }
    }

    val output = actor<ByteReadPacket>(CoroutineName("adnl-aes-encoder")) {
        var useCipher = false
        try {

        } finally {
            rawOutput.close()
        }
    }
}