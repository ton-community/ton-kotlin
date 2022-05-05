package org.ton.adnl

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

class EncryptedByteReadChannel(
        val encrypted: ByteReadChannel,
        val aes: AdnlAes,
) : ByteReadChannel by encrypted {
    override suspend fun readByte(): Byte = aes.encrypt {
        writeByte(encrypted.readByte())
    }.readByte()

    override suspend fun readDouble(): Double = aes.encrypt {
        writeDouble(encrypted.readDouble())
    }.readDouble()

    override suspend fun readFloat(): Float = aes.encrypt {
        writeFloat(encrypted.readFloat())
    }.readFloat()

    override suspend fun readFully(dst: ByteArray, offset: Int, length: Int) {
        val bytes = ByteArray(length)
        encrypted.readFully(bytes, 0, length)
        val encrypted = aes.encrypt(bytes)
        encrypted.copyInto(dst, offset)
    }

    override suspend fun readInt(): Int = aes.encrypt {
        writeInt(encrypted.readInt())
    }.readInt()

    override suspend fun readLong(): Long = aes.encrypt {
        writeLong(encrypted.readLong())
    }.readLong()

    override suspend fun readPacket(size: Int, headerSizeHint: Int): ByteReadPacket = aes.encrypt {
        writePacket(encrypted.readPacket(size, headerSizeHint))
    }

    override suspend fun readRemaining(limit: Long, headerSizeHint: Int): ByteReadPacket = aes.encrypt {
        writePacket(encrypted.readRemaining(limit, headerSizeHint))
    }
}