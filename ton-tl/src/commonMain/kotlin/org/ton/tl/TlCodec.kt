package org.ton.tl

import io.ktor.utils.io.core.*
import org.ton.tl.constructors.EnumTlCombinator


fun <R : Any> Output.writeTl(value: R, codec: TlCodec<R>) = codec.encode(this, value)
fun <R : Any> Output.writeTl(value: R, combinator: TlCombinator<R>) = combinator.encodeBoxed(this, value)
fun <R : Enum<R>> Output.writeTl(value: R, enum: EnumTlCombinator<R>) = enum.encodeBoxed(this, value)

interface TlCodec<T : Any> : TlDecoder<T>, TlEncoder<T>

