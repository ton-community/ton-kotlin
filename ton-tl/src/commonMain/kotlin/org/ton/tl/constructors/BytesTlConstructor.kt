package org.ton.tl.constructors

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import kotlin.reflect.typeOf

object BytesTlConstructor : TlConstructor<ByteArray>(
    type = typeOf<ByteArray>(),
    schema = "bytes data:string = Bytes"
) {
    override fun decode(input: Input): ByteArray {
        var size = input.readByteLength()
        size = (size shr 2) shl 2
        return input.readBytes(size)
    }

    override fun encode(output: Output, value: ByteArray) {
        val padding = calculatePadding(value.size)
        output.writeByteLength(value.size + padding)
        output.writeFully(value)
        repeat(padding) {
            output.writeByte(0)
        }
    }
}

fun Input.readBytesTl() = BytesTlConstructor.decode(this)
fun Output.writeBytesTl(byteArray: ByteArray) = BytesTlConstructor.encode(this, byteArray)