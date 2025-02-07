package org.ton.tl

import TLEncoder
import kotlinx.io.*
import kotlinx.io.bytestring.ByteString
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

public open class TL(
    public val boxed: Boolean,
    override val serializersModule: SerializersModule
) : BinaryFormat {
    public companion object Default : TL(false, EmptySerializersModule())
    public data object Boxed : TL(true, EmptySerializersModule())

    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
        val buffer = Buffer()
        encodeToSink(serializer, buffer, value)
        return buffer.readByteArray()
    }

    override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
        val buffer = Buffer()
        buffer.write(bytes)
        return decodeFromSource(deserializer, buffer)
    }

    public fun <T> decodeFromByteString(deserializer: DeserializationStrategy<T>, bytes: ByteString): T {
        val buffer = Buffer()
        buffer.write(bytes)
        return decodeFromSource(deserializer, buffer)
    }

    public fun <T> encodeToByteString(serializer: SerializationStrategy<T>, value: T): ByteString {
        val buffer = Buffer()
        encodeToSink(serializer, buffer, value)
        return buffer.readByteString()
    }

    public fun <T> encodeToSink(serializationStrategy: SerializationStrategy<T>, sink: Sink, value: T) {
        val encoder = TLEncoder(this, sink, intArrayOf(), boxed)
        encoder.encodeSerializableValue(serializationStrategy, value)
    }

    public fun <T> decodeFromSource(deserializer: DeserializationStrategy<T>, source: Source): T {
        val reader = TlReader(source)
        val decoder = TLDecoder(this, reader, intArrayOf(), boxed)
        return decoder.decodeSerializableValue(deserializer)
    }
}

public inline fun <reified T> TL.decodeFromSource(source: Source): T =
    decodeFromSource(serializersModule.serializer(), source)

public inline fun <reified T> TL.decodeFromByteString(bytes: ByteString): T =
    decodeFromByteString(serializersModule.serializer(), bytes)

public inline fun <reified T> TL.encodeToSink(sink: Sink, value: T) =
    encodeToSink(serializersModule.serializer(), sink, value)

public inline fun <reified T> TL.encodeToByteString(value: T): ByteString =
    encodeToByteString(serializersModule.serializer(), value)