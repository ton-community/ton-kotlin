package org.ton.tl

import io.ktor.util.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ton.crypto.encodeHex
import kotlin.experimental.and
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

@Serializable(ByteStringSerializer::class)
public open class ByteString internal constructor(
    internal val data: ByteArray
) : Comparable<ByteString> {
    private val hashCode by lazy(LazyThreadSafetyMode.PUBLICATION) {
        data.contentHashCode()
    }

    public val size: Int get() = data.size

    @JvmName("getByte")
    public operator fun get(index: Int): Byte = data[index]

    public fun toByteArray(): ByteArray = data.copyOf()

    public fun toByteArray(
        destination: ByteArray,
        destinationOffset: Int = 0,
        startIndex: Int = 0,
        endIndex: Int = size
    ): ByteArray =
        data.copyInto(destination, destinationOffset, startIndex, endIndex)

    public fun encodeHex(): String = org.ton.crypto.hex(data).uppercase()

    public fun decodeToString(): String = data.decodeToString()

    override fun compareTo(other: ByteString): Int {
        val sizeA = size
        val sizeB = other.size
        var i = 0
        val size = minOf(sizeA, sizeB)
        while (i < size) {
            val byteA = this[i] and 0xff.toByte()
            val byteB = other[i] and 0xff.toByte()
            if (byteA == byteB) {
                i++
                continue
            }
            return if (byteA < byteB) -1 else 1
        }
        if (sizeA == sizeB) return 0
        return if (sizeA < sizeB) -1 else 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ByteString) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int = hashCode

    override fun toString(): String = if (data.size <= 64) {
        "[hex=${encodeHex().uppercase()}]"
    } else {
        "[size=${data.size} hex=${data.copyOf(64).encodeHex()}]"
    }

    public companion object {
        @JvmStatic
        public fun of(vararg bytes: Byte): ByteString = ByteString(bytes)

        @JvmStatic
        @JvmName("of")
        public fun ByteArray.toByteString(fromIndex: Int = 0, toIndex: Int = size): ByteString =
            ByteString(copyOfRange(fromIndex, toIndex))

        @JvmStatic
        public fun String.decodeFromHex(): ByteString = ByteString(hex(this))
    }
}

public object ByteStringSerializer : KSerializer<ByteString> {
    override val descriptor: SerialDescriptor =
     PrimitiveSerialDescriptor("ByteString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ByteString {
        return ByteString(decoder.decodeString().decodeBase64Bytes())
    }

    override fun serialize(encoder: Encoder, value: ByteString) {
        encoder.encodeString(value.data.encodeBase64())
    }
}
