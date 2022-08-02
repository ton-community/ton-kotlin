package org.ton.adnl.client

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import org.ton.crypto.aes.AesCtr
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
        val actualHash = sha256(nonce, payloadBytes)
        check(hash.contentEquals(actualHash)) {
            "hash mismatch, expected: ${hash.encodeHex()} actual: ${actualHash.encodeHex()}\n$this"
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

suspend fun ByteWriteChannel.writeAdnlPacket(aes: AesCtr, adnlPacket: AdnlPacket) {
    val encryptedPacket = aes.encrypt(buildPacket {
        writeIntLittleEndian(adnlPacket.length)
        writeFully(adnlPacket.nonce)
        writePacket(adnlPacket.payload)
        writeFully(adnlPacket.hash)
    }.readBytes())
    writeFully(encryptedPacket)
}

suspend fun ByteReadChannel.readAdnlPacket(aes: AesCtr): AdnlPacket {
    val length = ByteReadPacket(aes.encrypt(readRemaining(4).readBytes())).readIntLittleEndian()
    if (length > (1 shl 24) || length < 32) {
        throw IllegalStateException("Invalid packet size: $length")
    }
    val data = readRemaining(length.toLong()).readBytes()
    val decryptedData = aes.encrypt(data)
    val nonce = decryptedData.copyOfRange(0, 32)
    val hash = decryptedData.copyOfRange(length - 32, length)
    val payload = decryptedData.copyOfRange(32, length - 32)
    return AdnlPacket(length, nonce, ByteReadPacket(payload), hash)
}
