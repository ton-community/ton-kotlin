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
        writeByte((byteArray.size + padding).toByte())
        writeFully(byteArray)
        repeat(padding) {
            writeByte(0)
        }
    }

    fun Output.calcPadding(size: Int): Int {
        return (size % 4).let { if (it > 0) 4 - it else 0 }
    }

    fun Input.readByteArray(): ByteArray {
        var size = readByte().toInt() and 0xFF
        if (size >= 254) {
            TODO()
        }
        println("remaining: $remaining")
        println("BYTES size: $size")
        val byteArray = ByteArray(size)
        readFully(byteArray)
        return byteArray
    }
}