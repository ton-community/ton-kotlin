package org.ton.tl.constructors

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import kotlin.reflect.typeOf

object IntTlConstructor : TlConstructor<Int>(
    type = typeOf<Int>(),
    schema = "int ? = Int"
) {
    const val SIZE_BYTES = Int.SIZE_BYTES

    override fun decode(input: Input): Int {
        return input.readIntLittleEndian()
    }

    override suspend fun decode(input: ByteReadChannel): Int {
        return input.readIntLittleEndian()
    }

    override fun encode(output: Output, value: Int) {
        output.writeIntLittleEndian(value)
    }
}

fun Input.readIntTl() = IntTlConstructor.decode(this)
fun Output.writeIntTl(value: Int) = IntTlConstructor.encode(this, value)
