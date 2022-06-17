package org.ton.tl.constructors

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import kotlin.reflect.typeOf

object Int256TlConstructor : TlConstructor<ByteArray>(
    type = typeOf<ByteArray>(),
    schema = "int256 8*[ int ] = Int256"
) {
    const val BYTE_SIZE = 8 * Int.SIZE_BYTES

    override fun decode(input: Input): ByteArray {
        return input.readBytes(BYTE_SIZE)
    }

    override fun encode(output: Output, value: ByteArray) {
        require(value.size == BYTE_SIZE) { "Invalid message size; expected: $BYTE_SIZE, actual: ${value.size}" }
        output.writeFully(value)
    }
}

fun Input.readInt256Tl() = Int256TlConstructor.decode(this)
fun Output.writeInt256Tl(message: ByteArray) = Int256TlConstructor.encode(this, message)
