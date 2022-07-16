package org.ton.tl

import io.ktor.utils.io.core.*
import org.ton.crypto.sha256

interface TlEncoder<T : Any> {
    fun encode(value: T): ByteArray = buildPacket {
        encode(this, value)
    }.readBytes()

    fun encode(output: Output, value: T)
    fun encodeBoxed(output: Output, value: T)

    fun encodeBoxed(value: T): ByteArray = buildPacket {
        encodeBoxed(this, value)
    }.readBytes()

    fun hash(value: T): ByteArray = sha256(encodeBoxed(value))
}

fun <R : Any> Output.writeOptionalTl(flag: Int, index: Int, encoder: TlEncoder<R>, value: R?) {
    if (value != null) {
        writeOptionalTl(flag, index) {
            encoder.encode(this, value)
        }
    }
}

fun Output.writeOptionalTl(flag: Int, index: Int, block: Output.() -> Unit) {
    if (flag and (1 shl index) != 0) {
        block()
    }
}