package org.ton.tl

import io.ktor.utils.io.core.*

interface TlEncoder<T : Any> {
    fun encode(value: T): ByteArray = buildPacket {
        encode(this, value)
    }.readBytes()

    fun encode(output: Output, value: T)
    fun encodeBoxed(output: Output, value: T)

    fun encodeBoxed(value: T): ByteArray = buildPacket {
        encodeBoxed(this, value)
    }.readBytes()

    fun Output.writeByteLength(length: Int) {
        if (length <= 253) {
            writeByte(length.toByte())
        } else {
            writeByte(254.toByte())
            writeByte((length and 0xFF).toByte())
            writeByte(((length and 0xFF00) shr 8).toByte())
            writeByte(((length and 0xFF0000) shr 16).toByte())
        }
    }
}

fun <R : Any> Output.writeFlagTl(flag: Int, index: Int, encoder: TlEncoder<R>, value: R?) {
    if (value != null) {
        writeFlagTl(flag, index) {
            encoder.encode(this, value)
        }
    }
}

fun Output.writeFlagTl(flag: Int, index: Int, block: Output.() -> Unit) {
    if (flag and (1 shl index) != 0) {
        block()
    }
}