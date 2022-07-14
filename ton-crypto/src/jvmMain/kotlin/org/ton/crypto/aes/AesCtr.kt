package org.ton.crypto.aes

import io.ktor.utils.io.core.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

actual class AesCtr actual constructor(
    key: ByteArray,
    iv: ByteArray,
) {
    private val secretKey: SecretKey = SecretKeySpec(key, "AES")
    private val iv = IvParameterSpec(iv)
    private val encryptCipher = Cipher.getInstance("AES/CTR/NoPadding").also {
        it.init(Cipher.ENCRYPT_MODE, secretKey, this.iv)
    }
    private val decryptCipher = Cipher.getInstance("AES/CTR/NoPadding").also {
        it.init(Cipher.DECRYPT_MODE, secretKey, this.iv)
    }

    actual fun encrypt(byteArray: ByteArray) = encryptCipher.update(byteArray)

    actual suspend fun encrypt(packet: suspend BytePacketBuilder.() -> Unit): ByteReadPacket {
        val builder = BytePacketBuilder()
        packet(builder)
        val encrypted = encrypt(builder.build().readBytes())
        return ByteReadPacket(encrypted)
    }
}