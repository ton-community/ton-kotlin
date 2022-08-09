package org.ton.adnl.client

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import org.ton.crypto.encodeHex
import org.ton.crypto.sha256
import kotlin.random.Random

class AdnlPacket(
    val length: Int,
    val nonce: ByteArray,
    val payload: ByteReadPacket,
    val hash: ByteArray
) {
    constructor(block: BytePacketBuilder.() -> Unit) : this(BytePacketBuilder().apply(block).build())

    constructor(payload: ByteReadPacket, nonce: ByteArray = Random.nextBytes(32)) : this(
        payload.remaining.toInt() + nonce.size + 32,
        nonce,
        payload,
        sha256(nonce, payload.copy().readBytes())
    )

    fun verify() {
        val payloadBytes = payload.copy().readBytes()
        val actualLength = payload.remaining.toInt() + nonce.size + 32
        check(length == actualLength) {
            "length mismatch, expected: $length actual: $actualLength\n$this"
        }
        val calculatedHash = sha256(nonce, payloadBytes)
        check(hash.contentEquals(calculatedHash)) {
            "hash mismatch, expected: ${calculatedHash.encodeHex()} actual: ${hash.encodeHex()}\n$this"
        }
    }

    fun isValid(): Boolean = try {
        verify()
        true
    } catch (e: IllegalStateException) {
        false
    }

    override fun toString(): String =
        "AdnlPacket(\n  length=$length\n  nonce=${nonce.encodeHex()}\n  payload=${
            payload.copy().readBytes().encodeHex()
        }\n  hash=${hash.encodeHex()}\n)"
}

suspend fun ByteWriteChannel.writeAdnlPacket(adnlPacket: AdnlPacket) {
    writePacket {
        writeIntLittleEndian(adnlPacket.length)
        writeFully(adnlPacket.nonce)
        writePacket(adnlPacket.payload)
        writeFully(adnlPacket.hash)
    }
}

suspend fun ByteReadChannel.readAdnlPacket(): AdnlPacket {
    val length = readIntLittleEndian()
    if (length > (1 shl 24) || length < 32) {
        throw IllegalStateException("Invalid packet size: $length")
    }
    val data = readRemaining(length.toLong()).readBytes()
    val nonce = data.copyOfRange(0, 32)
    val hash = data.copyOfRange(length - 32, length)
    val payload = data.copyOfRange(32, length - 32)
    return AdnlPacket(length, nonce, ByteReadPacket(payload), hash)
}
