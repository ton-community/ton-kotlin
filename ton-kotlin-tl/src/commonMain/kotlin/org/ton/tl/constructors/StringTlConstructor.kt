package org.ton.tl.constructors

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import kotlin.reflect.typeOf

object StringTlConstructor : TlConstructor<String>(
    type = typeOf<String>(),
    schema = "string ? = String"
) {
    override fun decode(input: Input): String {
        val bytes = input.readBytesTl()
        return bytes.decodeToString()
    }

    override fun encode(output: Output, value: String) {
        val bytes = value.encodeToByteArray()
        output.writeBytesTl(bytes)
    }
}

fun Input.readStringTl() = StringTlConstructor.decode(this)
fun Output.writeStringTl(value: String) = StringTlConstructor.encode(this, value)