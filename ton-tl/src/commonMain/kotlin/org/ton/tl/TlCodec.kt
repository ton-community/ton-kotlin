package org.ton.tl

import io.ktor.utils.io.core.*
import org.intellij.lang.annotations.Language
import org.ton.crypto.crc32
import kotlin.reflect.KClass

interface TlEncoder<T> {
    fun encode(message: T): ByteArray = buildPacket {
        encode(this, message)
    }.readBytes()

    fun encode(output: Output, message: T)

    fun <R> Output.writeTl(message: R, codec: TlCodec<R>) = codec.encode(this, message)
    fun <R : Any> Output.writeTl(message: R, combinator: TlCombinator<R>) = combinator.encodeBoxed(this, message)
    fun <R : Enum<R>> Output.writeTl(message: R, enum: TlEnum<R>) = enum.encodeBoxed(this, message)

    fun Output.writeByteArray(byteArray: ByteArray) {
        val padding = calcPadding(byteArray.size)
        val size = byteArray.size + padding
        if (size <= 253) {
            writeByte(size.toByte())
        } else {
            writeByte(254.toByte())
            writeByte((size and 0xFF).toByte())
            writeByte(((size and 0xFF00) shr 8).toByte())
            writeByte(((size and 0xFF0000) shr 16).toByte())
        }
        writeFully(byteArray)
        repeat(padding) {
            writeByte(0)
        }
    }

    fun Output.writeBits256(byteArray: ByteArray) {
        check(byteArray.size == 32)
        writeFully(byteArray)
    }

    fun <R : Any> Output.writeVector(message: List<R>, codec: TlCodec<R>) {
        writeIntLittleEndian(message.size)
        message.forEach {
            writeTl(it, codec)
        }
    }

    fun <R : Any> Output.writeVector(message: List<R>, combinator: TlCombinator<R>) {
        writeIntLittleEndian(message.size)
        message.forEach {
            writeTl(it, combinator)
        }
    }
}

interface TlDecoder<T> {
    fun decode(byteArray: ByteArray): T = decode(ByteReadPacket(byteArray))
    fun decode(input: Input): T

    fun <R> Input.readTl(codec: TlCodec<R>) = codec.decode(this)
    fun <R : Any> Input.readTl(combinator: TlCombinator<R>) = combinator.decodeBoxed(this)
    fun <R : Enum<R>> Input.readTl(enum: TlEnum<R>) = enum.decodeBoxed(this)

    fun Input.readByteArray(): ByteArray {
        var size = readByte().toInt() and 0xFF
        if (size >= 254) {
            size = (readByte().toInt() and 0xFF) or
                    ((readByte().toInt() and 0xFF) shl 8) or
                    ((readByte().toInt() and 0xFF) shl 16)
        }
        size += calcPadding(size)
        return readBytes(size)
    }

    fun Input.readBits256(): ByteArray = readBytes(32)

    fun <R : Any> Input.readVector(codec: TlCodec<R>): List<R> {
        val size = readIntLittleEndian()
        val list = List(size) {
            readTl(codec)
        }
        return list
    }

    fun <R : Any> Input.readVector(combinator: TlCombinator<R>): List<R> {
        val size = readIntLittleEndian()
        val list = List(size) {
            readTl(combinator)
        }
        return list
    }
}

private fun calcPadding(size: Int): Int = (size % 4).let { if (it > 0) 4 - it else 0 }

interface TlCodec<T> : TlEncoder<T>, TlDecoder<T>

abstract class TlConstructor<T : Any>(
    val type: KClass<T>,
    @Language("TL")
    val schema: String
) : TlCodec<T> {
    val id: Int = crc32(schema)

    fun encodeBoxed(message: T): ByteArray = buildPacket {
        encodeBoxed(this, message)
        val padding = calcPadding(size)
        repeat(padding) {
            writeByte(0)
        }
    }.readBytes()

    fun encodeBoxed(output: Output, message: T) {
        output.writeIntLittleEndian(id)
        encode(output, message)
    }

    fun <R : Any> Output.writeBoxedTl(message: R, codec: TlConstructor<R>) = codec.encodeBoxed(this, message)

    fun decodeBoxed(byteArray: ByteArray): T = decodeBoxed(ByteReadPacket(byteArray))
    fun decodeBoxed(input: Input): T {
        val actualId = input.readIntLittleEndian()
        require(actualId == id) { "Invalid ID. expected: $id actual: $actualId" }
        return decode(input)
    }

    fun <R : Any> Input.readBoxedTl(codec: TlConstructor<R>) = codec.decodeBoxed(this)

    override fun toString(): String = schema
}

abstract class TlCombinator<T : Any>(
        val constructors: List<TlConstructor<out T>>
) {
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
    fun findConstructor(type: KClass<out T>): TlConstructor<T> {
        val constructor = checkNotNull(
                constructors.find { it.type == type }
        ) {
            "Invalid type. actual: $type"
        }
        return constructor as TlConstructor<T>
    }

    fun decodeBoxed(byteArray: ByteArray): T = decodeBoxed(ByteReadPacket(byteArray))

    fun decodeBoxed(input: Input): T {
        val id = input.readIntLittleEndian()
        val constructor = findConstructor(id)
        return constructor.decode(input)
    }

    fun encodeBoxed(output: Output, message: T) {
        val type = message::class
        val constructor = findConstructor(type)
        constructor.encodeBoxed(output, message)
    }

    override fun toString(): String = constructors.joinToString("\n")
}

abstract class TlEnum<T : Enum<T>>(
        val constructors: Map<T, TlConstructor<out T>>
) {
    constructor(type: KClass<T>, vararg constructors: Pair<T, String>) : this(
            constructors.associate {
                it.first to object : TlConstructor<T>(
                        type = type,
                        schema = it.second
                ) {
                    override fun encode(output: Output, message: T) {
                    }

                    override fun decode(input: Input): T = it.first
                }
            }
    )

    fun findConstructor(id: Int): TlConstructor<out T> {
        val constructor = checkNotNull(
                constructors.values.find { it.id == id }
        ) {
            "Invalid ID. actual: $id"
        }
        return constructor
    }

    @Suppress("UNCHECKED_CAST")
    fun findConstructor(enum: T) = requireNotNull(
            constructors[enum]
    ) {
        "Invalid Enum. actual: $enum"
    } as TlConstructor<T>

    fun decodeBoxed(input: Input): T {
        val id = input.readIntLittleEndian()
        val constructor = findConstructor(id)
        return constructor.decode(input)
    }

    fun encodeBoxed(output: Output, enum: T) {
        val constructor = findConstructor(enum)
        output.writeIntLittleEndian(constructor.id)
        constructor.encode(enum)
    }
}