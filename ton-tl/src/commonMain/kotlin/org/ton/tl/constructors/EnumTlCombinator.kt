package org.ton.tl.constructors

import io.ktor.utils.io.core.*
import org.ton.tl.TlCombinator
import org.ton.tl.TlConstructor
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

open class EnumTlCombinator<T : Enum<T>>(
    val enumConstructors: Map<T, TlConstructor<out T>>
) : TlCombinator<T>(enumConstructors.values.toList()) {

    constructor(type: KClass<T>, vararg constructors: Pair<T, String>) : this(
        constructors.associate { (enum, schema) ->
            enum to EnumTlConstructor(enum, type, schema)
        }
    )

    @Suppress("UNCHECKED_CAST")
    fun findConstructor(enum: T) = requireNotNull(
        enumConstructors[enum]
    ) {
        "Invalid Enum. actual: $enum"
    } as TlConstructor<T>

    override fun encodeBoxed(output: Output, value: T) {
        val constructor = findConstructor(value)
        constructor.encodeBoxed(output, value)
    }

    private class EnumTlConstructor<T : Enum<T>>(
        val enum: T,
        type: KClass<T>,
        schema: String
    ) : TlConstructor<T>(type.createType(), schema) {
        override fun encode(output: Output, value: T) {
        }

        override fun decode(input: Input): T = enum
    }
}