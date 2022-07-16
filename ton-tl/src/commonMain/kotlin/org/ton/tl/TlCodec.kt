package org.ton.tl

import io.ktor.utils.io.core.*
import org.ton.tl.constructors.EnumTlCombinator


fun <R : Any> Output.writeTl(codec: TlCodec<R>, value: R) = codec.encode(this, value)
fun <R : Any> Output.writeTl(combinator: TlCombinator<R>, value: R) = combinator.encodeBoxed(this, value)
fun <R : Enum<R>> Output.writeTl(enum: EnumTlCombinator<R>, value: R) = enum.encodeBoxed(this, value)

interface TlCodec<T : Any> : TlDecoder<T>, TlEncoder<T>

interface TlObject<T : Any> {
    fun tlCodec(): TlCodec<T>
}

