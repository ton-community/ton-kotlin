package org.ton.adnl.aes

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import org.ton.crypto.aes.AesCtr

class AesByteWriteChannel(
    val channel: ByteWriteChannel,
    val aes: AesCtr,
) : ByteWriteChannel by channel {
    override suspend fun writeByte(b: Byte) = channel.writeByte(aes.encrypt { writeByte(b) }.readByte())

    override suspend fun writeDouble(d: Double) = channel.writeDouble(aes.encrypt { writeDouble(d) }.readDouble())

    override suspend fun writeFloat(f: Float) = channel.writeFloat(aes.encrypt { writeFloat(f) }.readFloat())

    override suspend fun writeFully(src: ByteArray, offset: Int, length: Int) {
        channel.writeFully(aes.encrypt(src.copyOfRange(offset, length)))
    }

    override suspend fun writeInt(i: Int) = channel.writeInt(aes.encrypt { writeInt(i) }.readInt())

    override suspend fun writeLong(l: Long) = channel.writeLong(aes.encrypt { writeLong(l) }.readLong())

    override suspend fun writePacket(packet: ByteReadPacket) = channel.writePacket(aes.encrypt {
        writePacket(packet)
    })

    override suspend fun writeShort(s: Short) = channel.writeShort(aes.encrypt { writeShort(s) }.readShort())
}