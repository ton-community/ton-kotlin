package org.ton.tl

import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ton.crypto.digest.sha256
import org.ton.crypto.encoding.base64
import kotlin.experimental.and
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

@Serializable(ByteStringSerializer::class)
public open class ByteString internal constructor(
    internal val data: ByteArray
) : Comparable<ByteString>, Collection<Byte> {
    private val hashCode by lazy(LazyThreadSafetyMode.PUBLICATION) {
        data.contentHashCode()
    }

    public override val size: Int get() = data.size

    override fun containsAll(elements: Collection<Byte>): Boolean =
        elements.all { contains(it) }

    override fun contains(element: Byte): Boolean =
        data.contains(element)

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

    public fun copyOf(newSize: Int): ByteString = ByteString(data.copyOf(newSize))

    public fun copyOfRange(fromIndex: Int, toIndex: Int): ByteString = ByteString(data.copyOfRange(fromIndex, toIndex))

    public fun copyInto(destination: ByteArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size): ByteArray =
        data.copyInto(destination, destinationOffset, startIndex, endIndex)

    public fun encodeHex(): String = org.ton.crypto.hex(data).uppercase()

    public fun encodeBase64(): String = base64(data)

    public fun decodeToString(): String = data.decodeToString()

    public fun hashSha256(): ByteString = sha256(data).asByteString()

    override fun isEmpty(): Boolean = data.isEmpty()

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

    override fun iterator(): ByteIterator = ByteIteratorImpl(data)

    override fun toString(): String = "0x${encodeHex().uppercase()}"

    private class ByteIteratorImpl(val collection: ByteArray) : ByteIterator() {
        var index: Int = 0

        override fun nextByte(): Byte {
            if (!hasNext()) throw NoSuchElementException("$index")
            return collection[index++]
        }

        override operator fun hasNext(): Boolean {
            return index < collection.size
        }
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

        @JvmStatic
        public fun String.decodeFromBase64(): ByteString = ByteString(base64(this))
    }
}

public fun ByteArray.asByteString(): ByteString = ByteString(this)

public fun Output.writeByteString(value: ByteString, offset: Int = 0, length: Int = value.size - offset) {
    writeFully(value.data, offset, length)
}

public fun Input.readByteString(size: Int): ByteString {
    val data = ByteArray(size)
    readFully(data)
    return ByteString(data)
}

public fun ByteReadPacket(
    src: ByteString,
    offset: Int = 0,
    length: Int = src.size - offset,
    block: (ByteArray) -> Unit = {}
): ByteReadPacket = ByteReadPacket(src.data, offset, length, block)

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
