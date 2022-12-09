package org.ton.tl

import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

abstract class AbstractTlCombinator<T : Any> : TlCodec<T> {
    abstract val baseClass: KClass<T>

    override fun decode(input: Input): T = decodeBoxed(input)

    override fun decodeBoxed(input: Input): T {
        val id = input.readIntLittleEndian()
        val constructor = findConstructorOrNull(id)
        requireNotNull(constructor) { "Unknown constructor ID: $id" }
        return constructor.decode(input)
    }

    override fun encode(output: Output, value: T) = encodeBoxed(output, value)

    override fun encodeBoxed(output: Output, value: T) {
        val constructor = findConstructorOrNull(value)
        requireNotNull(constructor) { "Unknown constructor for type: ${value::class}" }
        constructor.encodeBoxed(output, value)
    }

    abstract fun findConstructorOrNull(id: Int): TlDecoder<out T>?

    abstract fun findConstructorOrNull(value: T): TlEncoder<T>?
}
