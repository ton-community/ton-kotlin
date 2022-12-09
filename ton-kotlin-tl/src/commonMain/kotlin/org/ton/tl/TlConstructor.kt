package org.ton.tl

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import org.ton.crypto.crc32.crc32
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

abstract class TlConstructor<T : Any>(
    schema: String,
    id: Int? = null,
) : TlCodec<T> {
    val schema by lazy(LazyThreadSafetyMode.PUBLICATION) {
        schema
            .replace("(", "")
            .replace(")", "")
            .replace(";", "")
    }
    val id by lazy(LazyThreadSafetyMode.PUBLICATION) {
        id ?: crc32(schema.toByteArray())
    }

    override fun encodeBoxed(value: T): ByteArray = buildPacket {
        encodeBoxed(this, value)
    }.readBytes()

    override fun encodeBoxed(output: Output, value: T) {
        output.writeIntTl(id)
        encode(output, value)
    }

    fun <R : Any> Output.writeBoxedTl(codec: TlCodec<R>, value: R) = codec.encodeBoxed(this, value)

    override fun decodeBoxed(input: Input): T {
        val actualId = input.readIntTl()
        require(actualId == id) {
            val idHex = id.reverseByteOrder().toUInt().toString(16).padStart(8, '0')
            val actualHex = actualId.reverseByteOrder().toUInt().toString(16).padStart(8, '0')
            "Invalid ID. expected: $idHex ($id) actual: $actualHex ($actualId)"
        }
        return decode(input)
    }

    fun <R : Any> Input.readBoxedTl(codec: TlConstructor<R>) = codec.decodeBoxed(this)

    override fun toString(): String = schema

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TlConstructor<*>) return false
        if (id != other.id) return false
        if (schema != other.schema) return false
        return true
    }

    override fun hashCode(): Int = id
}
