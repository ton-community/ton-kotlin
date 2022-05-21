package org.ton.tl

import io.ktor.utils.io.core.*
import org.ton.tl.constructors.EnumTlCombinator

interface TlDecoder<T : Any> {
    fun decode(byteArray: ByteArray): T = decode(ByteReadPacket(byteArray))
    fun decode(input: Input): T

    fun decodeBoxed(byteArray: ByteArray): T = decodeBoxed(ByteReadPacket(byteArray))
    fun decodeBoxed(input: Input): T

    fun Input.readByteLength(): Int {
        var length = readByte().toInt() and 0xFF
        if (length >= 254) {
            length = (readByte().toInt() and 0xFF) or
                    ((readByte().toInt() and 0xFF) shl 8) or
                    ((readByte().toInt() and 0xFF) shl 16)
        }
        return length
    }

    // Same as readByteLength(), but returns number of bytes read as second pair element
    fun Input.readByteLengthEx(): Pair<Int, Int> {
        var length = readByte().toInt() and 0xFF
        if (length >= 254) {
            length = (readByte().toInt() and 0xFF) or
                    ((readByte().toInt() and 0xFF) shl 8) or
                    ((readByte().toInt() and 0xFF) shl 16)
            return Pair(length, 4)
        }
        return Pair(length, 1)
    }
}

fun <R : Any> Input.readTl(decoder: TlDecoder<R>) = decoder.decode(this)
fun <R : Any> Input.readTl(combinator: TlCombinator<R>) = combinator.decodeBoxed(this)
fun <R : Enum<R>> Input.readTl(enum: EnumTlCombinator<R>) = enum.decodeBoxed(this)