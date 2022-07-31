package org.ton.adnl.client

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import org.ton.crypto.aes.AesCtr
import org.ton.crypto.encodeHex
import org.ton.crypto.sha256

class AdnlPacket(
    val length: Int,
    val nonce: ByteArray,
    val data: ByteReadPacket,
    val hash: ByteArray
) {
    constructor(packet: ByteReadPacket, nonce: ByteArray) : this(
        packet.remaining.toInt() + nonce.size + 32,
        nonce,
        packet,
        sha256(nonce, packet.copy().readBytes())
    )

    override fun toString(): String =
        "AdnlPacket(length=$length, nonce=${nonce.encodeHex()}, data=$data, hash=${hash.encodeHex()})"
}

suspend fun ByteWriteChannel.writeAdnlPacket(aes: AesCtr, adnlPacket: AdnlPacket) {
    val encryptedPacket = aes.encrypt {
        writeIntLittleEndian(adnlPacket.length)
        writeFully(adnlPacket.nonce)
        writePacket(adnlPacket.data)
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
