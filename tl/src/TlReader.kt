package org.ton.tl

import kotlinx.io.*
import kotlinx.io.bytestring.ByteString
import org.ton.tl.constructors.BoolTlCombinator

public class TlReader(
    public val input: Source
) {
    public fun readBoolean(): Boolean = BoolTlCombinator.decode(this).value
    public fun readInt(): Int = input.readIntLe()
    public fun readLong(): Long = input.readLongLe()
    public fun readRaw(size: Int): ByteArray = input.readByteArray(size)

    public fun readByteString(size: Int): ByteString = input.readByteString(size)

    public fun readByteString(): ByteString = ByteString(*readBytes())

    public fun readBytes(): ByteArray {
        var resultLength = input.readUByte().toInt()
        var resultAlignedLength: Int = when {
            resultLength < 254 -> resultLength + 1
            resultLength == 254 -> {
                resultLength = input.readUByte().toInt() or
                        (input.readUByte().toInt() shl 8) or
                        (input.readUByte().toInt() shl 16)
                resultLength + 4
            }

            else -> {
                val resultLengthLong = input.readUByte().toLong() or
                        (input.readUByte().toLong() shl 8) or
                        (input.readUByte().toLong() shl 16) or
                        (input.readUByte().toLong() shl 24) or
                        (input.readUByte().toLong() shl 32) or
                        (input.readUByte().toLong() shl 40) or
                        (input.readUByte().toLong() shl 48)
                check(resultLengthLong <= Int.MAX_VALUE) {
                    "Too big byte array: $resultLengthLong"
                }
                resultLength = resultLengthLong.toInt()
                resultLength + 8
            }
        }
        val result = input.readByteArray(resultLength)
        input.skip(((4 - resultAlignedLength % 4) % 4).toLong())
        return result
    }

    public fun readString(): String = readBytes().decodeToString()

    public fun <T> readVector(block: TlReader.() -> T): List<T> {
        val size = readInt()
        return List(size) {
            block()
        }
    }
}

public inline operator fun <R> TlReader.invoke(block: TlReader.() -> R): R = block()

@Suppress("NOTHING_TO_INLINE")
public inline fun <T> TlReader.read(codec: TlCodec<T>): T = codec.decode(this)

public inline fun <E> TlReader.readNullable(flag: Int, index: Int, block: TlReader.() -> E): E? =
    readNullable(flag and (1 shl index) != 0, block)

public inline fun <E> TlReader.readNullable(check: Boolean, block: TlReader.() -> E): E? = if (check) block() else null
