package org.ton.adnl

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

class AesByteReadChannel(
    val channel: ByteReadChannel,
    val aes: AdnlAesCipher,
) : ByteReadChannel by channel {
    override suspend fun readByte(): Byte = aes.encrypt {
        writeByte(channel.readByte())
    }.readByte()

    override suspend fun readDouble(): Double = aes.encrypt {
        writeDouble(channel.readDouble())
    }.readDouble()

    override suspend fun readFloat(): Float = aes.encrypt {
        writeFloat(channel.readFloat())
    }.readFloat()

    override suspend fun readFully(dst: ByteArray, offset: Int, length: Int) {
        val bytes = ByteArray(length)
        channel.readFully(bytes, 0, length)
        val encrypted = aes.encrypt(bytes)
        encrypted.copyInto(dst, offset)
    }

    override suspend fun readInt(): Int = aes.encrypt {
        writeInt(channel.readInt())
    }.readInt()

    override suspend fun readLong(): Long = aes.encrypt {
        writeLong(channel.readLong())
    }.readLong()

    override suspend fun readPacket(size: Int): ByteReadPacket = aes.encrypt {
        writePacket(channel.readPacket(size))
    }

    override suspend fun readRemaining(limit: Long): ByteReadPacket = aes.encrypt {
        writePacket(channel.readRemaining(limit))
    }
}