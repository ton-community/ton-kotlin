package org.ton.adnl.aes

import io.ktor.utils.io.core.*
import org.ton.crypto.AesCtr

class AdnlAesCipher(
    key: ByteArray,
    ctr: ByteArray,
) {
    private val backend = AesCtr(key, ctr)

    suspend fun encrypt(packet: suspend BytePacketBuilder.() -> Unit): ByteReadPacket {
        val builder = BytePacketBuilder()
        packet(builder)
        val encrypted = encrypt(builder.build().readBytes())
        return ByteReadPacket(encrypted)
    }

    fun encrypt(byteArray: ByteArray): ByteArray {
        if (byteArray.isEmpty()) return byteArray
        return backend.encrypt(byteArray)
    }

    companion object {
        @Suppress("UnnecessaryVariable")
        fun secure(secret: ByteArray, digest: ByteArray): AdnlAesCipher {
            val x = secret
            val y = digest

            /*
                x[ 0], x[ 1], x[ 2], x[ 3], x[ 4], x[ 5], x[ 6], x[ 7],
                x[ 8], x[ 9], x[10], x[11], x[12], x[13], x[14], x[15],
                y[16], y[17], y[18], y[19], y[20], y[21], y[22], y[23],
                y[24], y[25], y[26], y[27], y[28], y[29], y[30], y[31]
             */
            val key = ByteArray(32)
            x.copyInto(key, destinationOffset = 0, startIndex = 0, endIndex = 16)
            y.copyInto(key, destinationOffset = 16, startIndex = 16, endIndex = 32)

            /*
                y[ 0], y[ 1], y[ 2], y[ 3], x[20], x[21], x[22], x[23],
                x[24], x[25], x[26], x[27], x[28], x[29], x[30], x[31]
             */
            val ctr = ByteArray(16)
            y.copyInto(ctr, destinationOffset = 0, startIndex = 0, endIndex = 4)
            x.copyInto(ctr, destinationOffset = 4, startIndex = 20, endIndex = 32)

            try {
                return AdnlAesCipher(key, ctr)
            } finally {
                key.fill(0)
                ctr.fill(0)
            }
        }
    }
}