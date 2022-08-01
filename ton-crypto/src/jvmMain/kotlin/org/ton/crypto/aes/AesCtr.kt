package org.ton.crypto.aes

import io.ktor.utils.io.core.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

actual class AesCtr actual constructor(
    key: ByteArray,
    iv: ByteArray,
) {
    private val _cipher = Cipher.getInstance("AES/CTR/NoPadding").also {
        it.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
    }

//    actual fun encrypt(byteArray: ByteArray): ByteArray {
//        if (byteArray.isEmpty()) return byteArray
//        return encrypt(ByteReadPacket(byteArray)).readBytes()
//    }
//
//    actual fun encrypt(byteReadPacket: ByteReadPacket): ByteReadPacket = byteReadPacket.readBytes()

    actual fun encrypt(byteArray: ByteArray): ByteArray {
        if (byteArray.isEmpty()) return byteArray
        return _cipher.update(byteArray)
    }

    actual fun encrypt(byteReadPacket: ByteReadPacket): ByteReadPacket =
        ByteReadPacket(encrypt(byteReadPacket.readBytes()))

    suspend fun encrypt(packet: suspend BytePacketBuilder.() -> Unit = {}): ByteReadPacket = encrypt(buildPacket {
        packet()
    })
}
