package org.ton.bitstring

import org.ton.bitstring.exception.BitStringUnderflowException
import org.ton.crypto.hex
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.jvm.JvmStatic
import kotlin.math.min

public open class ByteBackedBitString protected constructor(
    override val size: Int,
    public open val bytes: ByteArray
) : BitString {
    override operator fun get(index: Int): Boolean = getOrNull(index) ?: throw BitStringUnderflowException()

    override fun getOrNull(index: Int): Boolean? =
        if (index in 0..size) get(bytes, index) else null

    override fun plus(bytes: ByteArray): BitString = toMutableBitString().plus(bytes)
    override fun plus(bytes: ByteArray, bits: Int): BitString = toMutableBitString().plus(bytes, bits)

    // TODO: fix
//    override fun slice(startIndex: Int, endIndex: Int): BitString = slice(startIndex..endIndex)
//
//    override fun slice(indices: IntRange): BitString {
//        val result = ByteBackedMutableBitString.of(size = indices.last - indices.first)
//        for ((position, i) in indices.withIndex()) {
//            result[position] = get(i)
//        }
//        return result
//    }

    override fun toByteArray(augment: Boolean): ByteArray =
        if (augment && (size % 8 != 0)) {
            appendAugmentTag(bytes, size)
        } else {
            bytes.copyOf()
        }

    override fun toBooleanArray(): BooleanArray = toList().toBooleanArray()

    override fun toMutableBitString(): MutableBitString = ByteBackedMutableBitString.of(bytes, size)

    override fun iterator(): Iterator<Boolean> = BitStringIterator(this)

    override fun xor(other: BitString): BitString {
        return if (other !is ByteBackedBitString) {
            val result = ByteBackedMutableBitString.of(maxOf(size, other.size))
            for (i in 0 until min(size, other.size)) {
                result[i] = get(i) xor other[i]
            }
            result
        } else {
            val result = ByteArray(maxOf(bytes.size, other.bytes.size))
            for (i in 0 until min(bytes.size, other.bytes.size)) {
                result[i] = bytes[i] xor other.bytes[i]
            }
            of(result, maxOf(size, other.size))
        }
    }

    override fun or(other: BitString): BitString {
        return if (other !is ByteBackedBitString) {
            val result = ByteBackedMutableBitString.of(maxOf(size, other.size))
            for (i in 0 until min(size, other.size)) {
                result[i] = get(i) or other[i]
            }
            result
        } else {
            val result = ByteArray(maxOf(bytes.size, other.bytes.size))
            for (i in 0 until min(bytes.size, other.bytes.size)) {
                result[i] = bytes[i] or other.bytes[i]
            }
            of(result, maxOf(size, other.size))
        }
    }

    override fun toString(): String {
        if (size == 0) return ""
        val data = appendTag(bytes, size)
        val result = StringBuilder(hex(data))
        when (size % 8) {
            0 -> {
                result.deleteAt(result.lastIndex)
                result.deleteAt(result.lastIndex)
            }

            in 1..3 -> {
                result[result.lastIndex] = '_'
            }

            4 -> result.deleteAt(result.lastIndex)
            else -> result.append('_')
        }
        return result.toString().uppercase()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BitString) return false
        if (size != other.size) return false
        if (other is ByteBackedBitString) {
            if (!bytes.contentEquals(other.bytes)) return false
        } else {
            if (!toBooleanArray().contentEquals(other.toBooleanArray())) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = size
        result = 31 * result + bytes.contentHashCode()
        return result
    }

    internal open class BitStringIterator(
        val bitString: BitString,
        var index: Int = 0
    ) : ListIterator<Boolean> {
        override fun hasNext(): Boolean = index < bitString.size
        override fun hasPrevious(): Boolean = index - 1 >= 0
        override fun next(): Boolean = bitString[index++]
        override fun nextIndex(): Int = index + 1
        override fun previous(): Boolean = bitString[index--]
        override fun previousIndex(): Int = index - 1
    }

    private fun appendTag(data: ByteArray, bits: Int): ByteArray {
        val shift = bits % 8
        if (shift == 0 || data.isEmpty()) {
            val newData = data.copyOf(bits / 8 + 1)
            newData[newData.lastIndex] = 0x80.toByte()
            return newData
        } else {
            val newData = data.copyOf(bits / 8 + 1)
            var lastByte = newData[newData.lastIndex].toInt()
            if (shift != 7) {
                lastByte = lastByte shr (7 - shift)
            }
            lastByte = lastByte or 1
            if (shift != 7) {
                lastByte = lastByte shl (7 - shift)
            }
            newData[newData.lastIndex] = lastByte.toByte()
            return newData
        }
    }

    companion object {
        @JvmStatic
        fun of(size: Int = 0): ByteBackedBitString = ByteBackedBitString(size, constructByteArray(size))

        @JvmStatic
        fun of(
            bytes: ByteArray = ByteArray(0),
            size: Int = bytes.size * Byte.SIZE_BITS
        ): ByteBackedBitString = ByteBackedBitString(size, constructByteArray(bytes, size))

        @JvmStatic
        protected fun constructByteArray(bytes: ByteArray, size: Int): ByteArray {
            return bytes.copyOf(bytesSize(size))
        }

        @JvmStatic
        protected fun expandByteArray(bytes: ByteArray, size: Int): ByteArray {
            val requiredBytesSize = bytesSize(size)
            return if (bytes.size == requiredBytesSize) {
                bytes
            } else {
                constructByteArray(bytes, size)
            }
        }

        @JvmStatic
        protected fun constructByteArray(size: Int): ByteArray {
            return ByteArray(bytesSize(size))
        }

        @JvmStatic
        protected fun get(bytes: ByteArray, index: Int): Boolean {
            val byteIndex = index / Byte.SIZE_BITS
            val bitMask = (1 shl (7 - (index % Byte.SIZE_BITS))).toByte()
            return (bytes[byteIndex] and bitMask) != 0.toByte()
        }

        private fun bytesSize(bits: Int): Int {
            return bits / Byte.SIZE_BITS + if (bits % Byte.SIZE_BITS == 0) 0 else 1
        }
    }
}

internal inline val Int.byteIndex get() = this / Byte.SIZE_BITS
internal inline val Int.bitMask get() = (1 shl (7 - (this % Byte.SIZE_BITS))).toByte()

private fun appendAugmentTag(data: ByteArray, bits: Int): ByteArray {
    val shift = bits % Byte.SIZE_BITS
    if (shift == 0 || data.isEmpty()) {
        val newData = data.copyOf(bits / Byte.SIZE_BITS + 1)
        newData[newData.lastIndex] = 0x80.toByte()
        return newData
    } else {
        val newData = data.copyOf(bits / Byte.SIZE_BITS + 1)
        var lastByte = newData[newData.lastIndex].toInt() and 0xFF
        if (shift != 7) {
            lastByte = lastByte shr (7 - shift)
        }
        lastByte = lastByte or 1
        if (shift != 7) {
            lastByte = lastByte shl (7 - shift)
        }
        newData[newData.lastIndex] = lastByte.toByte()
        return newData
    }
}
