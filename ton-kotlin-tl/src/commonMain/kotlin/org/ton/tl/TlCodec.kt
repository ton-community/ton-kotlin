package org.ton.tl

import io.ktor.utils.io.core.*
import org.ton.tl.constructors.EnumTlCombinator



fun <R> Output.writeTl(codec: TlCodec<R>, value: R) = codec.encode(this, value)
fun <R : Any> Output.writeTl(combinator: TlCombinator<R>, value: R) = combinator.encodeBoxed(this, value)
fun <R : Enum<R>> Output.writeTl(enum: EnumTlCombinator<R>, value: R) = enum.encodeBoxed(this, value)

interface TlCodec<T> : TlDecoder<T>, TlEncoder<T>

interface TlObject<T> where T : TlObject<T> {
    fun tlCodec(): TlCodec<out T>

    @Suppress("UNCHECKED_CAST")
    fun hash(): ByteArray = (tlCodec() as TlCodec<T>).hash(this as T)

    @Suppress("UNCHECKED_CAST")
    fun toByteArray(): ByteArray {
        val codec = tlCodec() as TlCodec<T>
        return codec.encodeBoxed(this as T)
    }
}

interface TLFunction<Q : TLFunction<Q, A>, A : TlObject<A>> : TlObject<Q> {
    override fun tlCodec(): TlCodec<Q>
    fun resultTlCodec(): TlCodec<A>

    @Suppress("UNCHECKED_CAST")
    override fun toByteArray(): ByteArray {
        val codec = tlCodec()
        return codec.encodeBoxed(this as Q)
    }
}
