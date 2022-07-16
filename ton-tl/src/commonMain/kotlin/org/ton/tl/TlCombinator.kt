package org.ton.tl

import io.ktor.utils.io.core.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSupertypeOf

abstract class TlCombinator<T : Any>(
    val constructors: List<TlConstructor<out T>>
) : TlCodec<T> {
    constructor(vararg constructors: TlConstructor<out T>) : this(constructors.toList())

    fun findConstructor(id: Int): TlConstructor<out T> {
        val constructor = checkNotNull(
            constructors.find { it.id == id }
        ) {
            "Invalid ID. actual: $id"
        }
        return constructor
    }

    @Suppress("UNCHECKED_CAST")
    fun findConstructor(type: KClass<out T>): TlConstructor<T> = findConstructor(type.createType())

    @Suppress("UNCHECKED_CAST")
    fun findConstructor(type: KType): TlConstructor<T> {
        val constructor = checkNotNull(
            constructors.find { constructor ->
                constructor.type.isSupertypeOf(type)
            }
        ) {
            "Invalid type. actual: $type"
        }
        return constructor as TlConstructor<T>
    }

    override fun decode(input: Input): T = decode(input)

    override fun decodeBoxed(input: Input): T {
        val id = input.readIntLittleEndian()
        val constructor = findConstructor(id)
        return constructor.decode(input)
    }

    override fun encode(output: Output, value: T) = encodeBoxed(output, value)

    override fun encodeBoxed(output: Output, value: T) {
        val type = value::class
        val constructor = findConstructor(type)
        constructor.encodeBoxed(output, value)
    }

    override fun toString(): String = constructors.joinToString("\n")
}