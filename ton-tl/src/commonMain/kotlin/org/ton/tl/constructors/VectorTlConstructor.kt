package org.ton.tl.constructors

import io.ktor.utils.io.core.*
import org.ton.tl.TlCombinator
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl
import kotlin.reflect.typeOf

class VectorTlConstructor<T : Any>(
    val elementConstructor: TlConstructor<T>
) : TlConstructor<List<T>>(
    type = typeOf<List<T>>(),
    schema = "vector {t:Type} # [ t ] = Vector t"
) {
    override fun encode(output: Output, value: List<T>) = encode(output, value, elementConstructor)

    override fun decode(input: Input): List<T> = decode(input, elementConstructor)

    companion object {
        fun <T : Any> encode(output: Output, value: List<T>, constructor: TlConstructor<T>) {
            output.writeIntLittleEndian(value.size)
            value.forEach { element ->
                output.writeTl(constructor, element)
            }
        }

        fun <T : Any> decode(input: Input, constructor: TlConstructor<T>): List<T> {
            val size = input.readIntLittleEndian()
            return List(size) {
                input.readTl(constructor)
            }
        }
    }
}

fun <T : Any> Input.readVectorTl(constructor: TlConstructor<T>) =
    VectorTlConstructor.decode(this, constructor)

fun <T : Any> Output.writeVectorTl(value: List<T>, constructor: TlConstructor<T>) =
    VectorTlConstructor.encode(this, value, constructor)

class VectorTlCombinator<T : Any>(
    val elementCombinator: TlCombinator<T>
) : TlConstructor<List<T>>(
    type = typeOf<List<T>>(),
    schema = "vector {t:Type} # [ t ] = Vector t"
) {
    override fun encode(output: Output, value: List<T>) = encode(output, value, elementCombinator)

    override fun decode(input: Input): List<T> = decode(input, elementCombinator)

    companion object {
        fun <T : Any> encode(output: Output, value: List<T>, constructor: TlCombinator<T>) {
            output.writeIntLittleEndian(value.size)
            value.forEach { element ->
                output.writeTl(constructor, element)
            }
        }

        fun <T : Any> decode(input: Input, constructor: TlCombinator<T>): List<T> {
            val size = input.readIntLittleEndian()
            return List(size) {
                input.readTl(constructor)
            }
        }
    }
}

fun <T : Any> Input.readVectorTl(combinator: TlCombinator<T>) =
    VectorTlCombinator.decode(this, combinator)

fun <T : Any> Output.writeVectorTl(value: List<T>, combinator: TlCombinator<T>) =
    VectorTlCombinator.encode(this, value, combinator)
