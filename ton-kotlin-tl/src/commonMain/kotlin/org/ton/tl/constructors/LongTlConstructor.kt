package org.ton.tl.constructors

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import kotlin.reflect.typeOf

object LongTlConstructor : TlConstructor<Long>(
    schema = "long ? = Long"
) {
    override fun decode(input: Input): Long {
        return input.readLongLittleEndian()
    }

    override fun encode(output: Output, value: Long) {
        output.writeLongLittleEndian(value)
    }
}

fun Input.readLongTl() = LongTlConstructor.decode(this)
fun Output.writeLongTl(value: Long) = LongTlConstructor.encode(this, value)
