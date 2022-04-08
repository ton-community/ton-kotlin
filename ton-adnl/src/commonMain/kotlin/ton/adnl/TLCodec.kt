package ton.adnl

import io.ktor.utils.io.core.*

interface TLCodec<T> {
    val id: Int

    fun encode(message: T): ByteArray = buildPacket {
        encode(this, message)
    }.readBytes()

    fun encode(output: Output, message: T)

    fun encodeBoxed(message: T): ByteArray = buildPacket {
        encodeBoxed(this, message)
        val padding = calcPadding(size)
        repeat(padding) {
            writeByte(0)
        }
    }.readBytes()

    fun encodeBoxed(output: Output, message: T) {
        output.writeIntLittleEndian(id)
        encode(output, message)
    }

    fun decode(byteArray: ByteArray): T = decode(ByteReadPacket(byteArray))
    fun decode(input: Input): T
    fun decodeBoxed(byteArray: ByteArray): T = decodeBoxed(ByteReadPacket(byteArray))
    fun decodeBoxed(input: Input): T {
        val actualId = input.readIntLittleEndian()
        require(actualId == id) { "Invalid ID. expected: $id actual: $actualId" }
        return decode(input)
    }

    fun <R> Input.readTl(codec: TLCodec<R>) = codec.decode(this)
    fun <R> Input.readBoxedTl(codec: TLCodec<R>) = codec.decodeBoxed(this)

    fun <R> Output.writeTl(message: R, codec: TLCodec<R>) = codec.encode(this, message)
    fun <R> Output.writeBoxedTl(message: R, codec: TLCodec<R>) = codec.encodeBoxed(this, message)

    fun Output.writeByteArray(byteArray: ByteArray) {
        val padding = calcPadding(byteArray.size)
        val size = byteArray.size + padding
        if (size <= 253) {
            writeByte(size.toByte())
        } else {
            writeByte(254.toByte())
            writeByte((size and 0xFF).toByte())
            writeByte(((size and 0xFF00) shr 8).toByte())
            writeByte(((size and 0xFF0000) shr 16).toByte())
        }
        writeFully(byteArray)
        repeat(padding) {
            writeByte(0)
        }
    }

    fun Input.readByteArray(): ByteArray {
        var size = readByte().toInt() and 0xFF
        if (size >= 254) {
            size = (readByte().toInt() and 0xFF) or
                    ((readByte().toInt() and 0xFF) shl 8) or
                    ((readByte().toInt() and 0xFF) shl 16)
        }
        size += calcPadding(size)
        return readBytes(size)
    }

    private fun calcPadding(size: Int): Int = (size % 4).let { if (it > 0) 4 - it else 0 }
}