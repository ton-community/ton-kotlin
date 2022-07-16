package org.ton.tl.constructors

import io.ktor.utils.io.core.*
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl
import kotlin.reflect.typeOf

class VectorTlConstructor<T : Any>(
    val elementConstructor: TlCodec<T>
) : TlConstructor<List<T>>(
    type = typeOf<List<T>>(),
    schema = "vector {t:Type} # [ t ] = Vector t"
) {
    override fun encode(output: Output, value: List<T>) = encode(output, value, elementConstructor)

    override fun decode(input: Input): List<T> = decode(input, elementConstructor)

    companion object {
        fun <T : Any> encode(output: Output, value: List<T>, constructor: TlCodec<T>) {
            output.writeIntLittleEndian(value.size)
            value.forEach { element ->
                output.writeTl(constructor, element)
            }
        }

        fun <T : Any> decode(input: Input, constructor: TlCodec<T>): List<T> {
            val size = input.readIntLittleEndian()
            return List(size) {
                input.readTl(constructor)
            }
        }
    }
}

fun <T : Any> Input.readVectorTl(constructor: TlCodec<T>) =
    VectorTlConstructor.decode(this, constructor)

fun <T : Any> Output.writeVectorTl(value: List<T>, constructor: TlCodec<T>) =
    VectorTlConstructor.encode(this, value, constructor)
