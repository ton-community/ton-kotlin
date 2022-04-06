package ton.adnl

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

class EncryptedByteWriteChannel(
    val encrypted: ByteWriteChannel,
    val aes: AdnlAes,
) : ByteWriteChannel by encrypted {
    override suspend fun writeByte(b: Byte) = encrypted.writeByte(aes.encrypt { writeByte(b) }.readByte())

    override suspend fun writeDouble(d: Double) = encrypted.writeDouble(aes.encrypt { writeDouble(d) }.readDouble())

    override suspend fun writeFloat(f: Float) = encrypted.writeFloat(aes.encrypt { writeFloat(f) }.readFloat())

    override suspend fun writeFully(src: ByteArray, offset: Int, length: Int) {
        encrypted.writeFully(aes.encrypt(src.copyOfRange(offset, length)))
    }

    override suspend fun writeInt(i: Int) = encrypted.writeInt(aes.encrypt { writeInt(i) }.readInt())

    override suspend fun writeLong(l: Long) = encrypted.writeLong(aes.encrypt { writeLong(l) }.readLong())

    override suspend fun writePacket(packet: ByteReadPacket) = encrypted.writePacket(aes.encrypt {
        writePacket(packet)
    })

    override suspend fun writeShort(s: Short) = encrypted.writeShort(aes.encrypt { writeShort(s) }.readShort())
}