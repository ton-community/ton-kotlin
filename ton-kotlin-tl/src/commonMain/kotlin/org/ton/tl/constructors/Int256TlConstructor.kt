package org.ton.tl.constructors

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import org.ton.bitstring.BitString
import org.ton.tl.TlConstructor
import kotlin.reflect.typeOf

object Int256TlConstructor : TlConstructor<ByteArray>(
    schema = "int256 8*[ int ] = Int256"
) {
    const val SIZE_BYTES = 8 * Int.SIZE_BYTES

    override fun decode(input: Input): ByteArray {
        return input.readBytes(SIZE_BYTES)
    }

    override fun encode(output: Output, value: ByteArray) {
        require(value.size == SIZE_BYTES) { "Invalid message size; expected: $SIZE_BYTES, actual: ${value.size}" }
        output.writeFully(value)
    }
}

fun Input.readInt256Tl() = Int256TlConstructor.decode(this)
fun Output.writeInt256Tl(message: ByteArray) = Int256TlConstructor.encode(this, message)
fun Output.writeInt256Tl(message: BitString) = Int256TlConstructor.encode(this, message.toByteArray())
