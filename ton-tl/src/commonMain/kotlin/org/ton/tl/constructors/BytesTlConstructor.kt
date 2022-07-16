@file:Suppress("OPT_IN_USAGE")

package org.ton.tl.constructors

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import kotlin.reflect.typeOf

object BytesTlConstructor : TlConstructor<ByteArray>(
    type = typeOf<ByteArray>(),
    schema = "bytes data:string = Bytes"
) {
    override fun decode(input: Input): ByteArray {
        var resultLength = input.readUByte().toInt()
        var resultAlignedLength: Int
        if (resultLength < 254) {
            resultAlignedLength = resultLength + 1
        } else if (resultLength == 254) {
            resultLength = input.readUByte().toInt() +
                    (input.readUByte().toInt() shl 8) +
                    (input.readUByte().toInt() shl 16)
            resultAlignedLength = resultLength + 4
        } else {
            val resultLengthLong = input.readUByte().toLong() +
                    (input.readUByte().toLong() shl 8) +
                    (input.readUByte().toLong() shl 16) +
                    (input.readUByte().toLong() shl 24) +
                    (input.readUByte().toLong() shl 32) +
                    (input.readUByte().toLong() shl 40) +
                    (input.readUByte().toLong() shl 48)
            if (resultLengthLong > Int.MAX_VALUE) {
                throw IllegalStateException("Too big byte array: $resultLengthLong")
            }
            resultLength = resultLengthLong.toInt()
            resultAlignedLength = resultLength + 8
        }
        val result = input.readBytes(resultLength)
        while (resultAlignedLength++ % 4 > 0) {
            input.discardExact(1)
        }
        return result
    }

    override fun encode(output: Output, value: ByteArray) {
        var length = value.size
        if (length < 254) {
            output.writeUByte(length.toUByte())
            length++
        } else if (length < (1 shl 24)) {
            output.writeUByte(254u)
            output.writeUByte((length and 255).toUByte())
            output.writeUByte(((length shr 8) and 255).toUByte())
            output.writeUByte((length shr 16).toUByte())
            length += 4
        } else if (length < Int.MAX_VALUE) {
            output.writeUByte(255u)
            output.writeUByte((length and 255).toUByte())
            output.writeUByte(((length shr 8) and 255).toUByte())
            output.writeUByte(((length shr 16) and 255).toUByte())
            output.writeUByte(((length shr 24) and 255).toUByte())
            output.writeByte(0)
            output.writeByte(0)
            output.writeByte(0)
            length += 8
        } else {
            throw IllegalStateException("Too big byte array: $length")
        }
        output.writeFully(value)
        while (length++ % 4 > 0) {
            output.writeByte(0)
        }
    }
}

fun Input.readBytesTl() = BytesTlConstructor.decode(this)
fun Output.writeBytesTl(byteArray: ByteArray) = BytesTlConstructor.encode(this, byteArray)