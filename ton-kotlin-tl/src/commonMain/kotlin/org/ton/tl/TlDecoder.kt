package org.ton.tl

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import org.ton.tl.constructors.EnumTlCombinator

interface TlDecoder<T : Any> {
    fun decode(byteArray: ByteArray): T = decode(ByteReadPacket(byteArray))
    fun decode(input: Input): T
    suspend fun decode(input: ByteReadChannel): T = TODO()
    fun decode(values: Iterator<*>): T = TODO(this.toString())

    fun decodeBoxed(byteArray: ByteArray): T = decodeBoxed(ByteReadPacket(byteArray))
    fun decodeBoxed(input: Input): T
    suspend fun decodeBoxed(input: ByteReadChannel): T = TODO()
}

fun <R : Any> Input.readFlagTl(flag: Int, index: Int, decoder: TlDecoder<R>) =
    readFlagTl(flag, index) { decoder.decode(this) }

fun <R : Any> Input.readFlagTl(flag: Int, index: Int, block: Input.() -> R) =
    if (flag and (1 shl index) != 0) {
        block()
    } else null

fun <R : Any> Input.readTl(decoder: TlDecoder<R>) = decoder.decode(this)
fun <R : Any> Input.readTl(combinator: TlCombinator<R>) = combinator.decodeBoxed(this)
fun <R : Enum<R>> Input.readTl(enum: EnumTlCombinator<R>) = enum.decodeBoxed(this)
