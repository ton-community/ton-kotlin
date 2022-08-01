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

    override fun toString(): String =
        "AdnlPacket(length=$length, nonce=${nonce.encodeHex()}, payload=$payload, hash=${hash.encodeHex()})"
}

suspend fun ByteWriteChannel.writeAdnlPacket(aes: AesCtr, adnlPacket: AdnlPacket) {
    val encryptedPacket = aes.encrypt {
        writeIntLittleEndian(adnlPacket.length)
        writeFully(adnlPacket.nonce)
        writePacket(adnlPacket.payload)
        writeFully(adnlPacket.hash)
    }
    writePacket(encryptedPacket)
}

suspend fun ByteReadChannel.readAdnlPacket(aes: AesCtr): AdnlPacket {
    val length = aes.encrypt {
        writeIntLittleEndian(readIntLittleEndian())
    }.readIntLittleEndian()
    val nonce = aes.encrypt(readPacket(32)).readBytes()
    val data = aes.encrypt(readPacket(length - 64))
    val hash = aes.encrypt(readPacket(32)).readBytes()
    return AdnlPacket(length, nonce, data, hash)
}
