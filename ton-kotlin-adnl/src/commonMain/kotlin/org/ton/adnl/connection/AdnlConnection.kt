package org.ton.adnl.connection

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.ton.crypto.aes.AesCtr
import org.ton.crypto.encodeHex
import org.ton.crypto.random.SecureRandom
import org.ton.crypto.sha256.sha256
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class AdnlConnection(
    val isClient: Boolean,
    private val networkInput: ByteReadChannel,
    private val networkOutput: ByteWriteChannel,
    override val coroutineContext: CoroutineContext,
    private val onDone: () -> Unit
) : CoroutineScope {
    val timeout get() = if (isClient) 15.seconds else 60.seconds
    private lateinit var inputCipher: AesCtr
    private lateinit var outputCipher: AesCtr
    private val isCryptoInitialized = atomic(false)
    private val lastActivity = atomic(Clock.System.now())
    private val timeoutJob = launch(coroutineContext + CoroutineName("timeout")) {
        try {
            while (true) {
                val remaining = (lastActivity.value + timeout) - Clock.System.now()
                if (remaining <= Duration.ZERO) {
                    break
                }
                delay(remaining)
            }
        } catch (_: Throwable) {
        } finally {
            networkInput.cancel()
            networkOutput.close()
            onDone()
        }
    }
    private val sendChannel = Channel<ByteReadPacket>(Channel.UNLIMITED)
    protected val sendJob = launch(coroutineContext + CoroutineName("send")) {
        for (packet in sendChannel) {
            networkOutput.writePacket(packet)
            networkOutput.flush()
        }
    }

    fun initCrypto(nonce: ByteArray) {
        require(nonce.size >= 96) { "too small crypto nonce, expected: 96, actual: ${nonce.size}" }
        val s1 = nonce.copyOfRange(0, 32)
        val s2 = nonce.copyOfRange(32, 64)
        val v1 = nonce.copyOfRange(64, 80)
        val v2 = nonce.copyOfRange(80, 96)
        if (isClient) {
            inputCipher = AesCtr(s1, v1)
            outputCipher = AesCtr(s2, v2)
        } else {
            inputCipher = AesCtr(s2, v2)
            outputCipher = AesCtr(s1, v1)
        }
        isCryptoInitialized.value = true
    }

    protected suspend fun sendRaw(packet: BytePacketBuilder.() -> Unit) = sendRaw(buildPacket(packet))
    protected suspend fun sendRaw(packet: ByteReadPacket) = sendChannel.send(packet)

    open suspend fun send(packet: BytePacketBuilder.() -> Unit) = send(buildPacket(packet))
    open suspend fun send(packet: ByteReadPacket) {
        val dataSize = packet.remaining.toInt() + 32 + 32
        require(dataSize in 32..(1 shl 24)) { "Invalid packet size: $dataSize" }

        val nonce = SecureRandom.nextBytes(32) // TODO: return random
        val payload = packet.readBytes()
        val hash = sha256(nonce, payload)
        val data = buildPacket {
            writeIntLittleEndian(dataSize)
            writeFully(nonce)
            writeFully(payload)
            writeFully(hash)
        }

        val encryptedData = outputCipher.update(data.readBytes())
        sendRaw(ByteReadPacket(encryptedData))
    }

    suspend fun receive(byteReadChannel: ByteReadChannel) {
        if (isCryptoInitialized.value) {
            val encryptedLength = ByteArray(4).apply {
                byteReadChannel.readFully(this)
            }
            val length = ByteReadPacket(inputCipher.update(encryptedLength)).readIntLittleEndian()
            check(length in 32..(1 shl 24)) { "Invalid length" }
            val encryptedData = ByteArray(length).apply {
                byteReadChannel.readFully(this)
            }
            val data = ByteReadPacket(inputCipher.update(encryptedData))
            lastActivity.value = Clock.System.now()
            receivePacket(data)
        } else {
            val packet = byteReadChannel.readPacket(256)
            lastActivity.value = Clock.System.now()
            handleInitPacket(packet)
        }
    }

    suspend fun receivePacket(data: ByteReadPacket) {
        val payload = data.readBytes((data.remaining - 32).toInt())
        val hash = data.readBytes(32)

        require(sha256(payload).contentEquals(hash)) {
            "sha256 mismatch"
        }

        if (payload.size == 32) {
            // keepalive
            return
        }

        val packet = ByteReadPacket(payload)
        packet.discard(32) // nonce

        if (handleCustomPacket(packet)) return
        handlePacket(packet)
    }

    abstract suspend fun handleInitPacket(data: ByteReadPacket)
    abstract suspend fun handleCustomPacket(data: ByteReadPacket): Boolean
    abstract suspend fun handlePacket(data: ByteReadPacket)
}
