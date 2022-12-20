package org.ton.bitstring

import org.ton.bitstring.exception.BitStringOverflowException
import org.ton.bitstring.exception.BitStringUnderflowException
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.jvm.JvmStatic
import kotlin.math.min

public open class ByteBackedBitString constructor(
    override val size: Int,
    public open val data: ByteArray,
    public open val offset: Int = 0,
    public open val length: Int = data.size - offset
) : BitString {
    override operator fun get(index: Int): Boolean = getOrNull(index) ?: throw BitStringUnderflowException()

    override fun getOrNull(index: Int): Boolean? =
        if (index in 0..size) get(data, index) else null

    override fun plus(bits: BooleanArray): BitString = toMutableBitString().plus(bits)
    override fun plus(bits: Iterable<Boolean>): BitString = toMutableBitString().plus(bits)
    override fun plus(bits: Collection<Boolean>): BitString = toMutableBitString().plus(bits)
    override fun plus(bytes: ByteArray): BitString = toMutableBitString().plus(bytes)
    override fun plus(bytes: ByteArray, bits: Int): BitString = toMutableBitString().plus(bytes, bits)

    override fun slice(indices: IntRange): BitString {
        val result = ByteBackedMutableBitString.of(size = indices.last - indices.first + 1)
        for ((position, i) in indices.withIndex()) {
            result[position] = get(i)
        }
        return result
    }

    override fun toByteArray(augment: Boolean): ByteArray =
        if (augment && (size % 8 != 0)) {
            BitString.appendAugmentTag(data, size)
        } else {
            data.copyOf()
        }

    override fun toBooleanArray(): BooleanArray = (this as List<Boolean>).toBooleanArray()

    override fun toMutableBitString(): MutableBitString = ByteBackedMutableBitString.of(data, size)

    override fun contains(element: Boolean): Boolean = any { it == element }
    override fun containsAll(elements: Collection<Boolean>): Boolean = elements.all { contains(it) }
    override fun isEmpty(): Boolean = size == 0
    override fun lastIndexOf(element: Boolean): Int = indexOfLast { it == element }
    override fun listIterator(): ListIterator<Boolean> = BitStringIterator(this)
    override fun listIterator(index: Int): ListIterator<Boolean> = BitStringIterator(this, index)
    override fun subList(fromIndex: Int, toIndex: Int): BitString = slice(fromIndex..toIndex)
    override fun indexOf(element: Boolean): Int = indexOfFirst { it == element }
    override fun iterator(): Iterator<Boolean> = listIterator()

    override fun xor(other: BitString): BitString {
        require(size == other.size) { "BitStrings must be of the same size" }
        return if (other !is ByteBackedBitString) {
            val result = ByteBackedMutableBitString.of(size)
            for (i in 0 until size) {
                result[i] = get(i) xor other[i]
            }
            result
        } else {
            val result = ByteArray(data.size)
            for (i in data.indices) {
                result[i] = data[i] xor other.data[i]
            }
            of(result, size)
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
            val result = ByteArray(maxOf(data.size, other.data.size))
            for (i in 0 until min(data.size, other.data.size)) {
                result[i] = data[i] or other.data[i]
            }
            of(result, maxOf(size, other.size))
        }
    }

    override fun toString(): String {
        if (size == 0) return ""
        val data = appendTag(data, size)
        val result = StringBuilder()
        for (i in offset until offset + length) {
            result.append(data[i].toUByte().toString(16).uppercase())
        }
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
        return result.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BitString) return false
        if (size != other.size) return false
        if (other is ByteBackedBitString) {
            if (!data.contentEquals(other.data)) return false
        } else {
            if (!toBooleanArray().contentEquals(other.toBooleanArray())) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = size
        result = 31 * result + data.contentHashCode()
        return result
    }

    internal open class BitStringIterator(
        open val bitString: BitString,
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

    public companion object {
        @JvmStatic
        public fun of(size: Int = 0): ByteBackedBitString = ByteBackedBitString(size, constructByteArray(size))

        @JvmStatic
        public fun of(
            bytes: ByteArray = ByteArray(0),
            size: Int = bytes.size * Byte.SIZE_BITS
        ): ByteBackedBitString = ByteBackedBitString(size, constructByteArray(bytes, size))

        @JvmStatic
        protected fun constructByteArray(bytes: ByteArray, size: Int): ByteArray {
            checkSize(size)
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
            checkSize(size)
            return ByteArray(bytesSize(size))
        }

        @JvmStatic
        protected fun checkSize(size: Int): Unit = require(size in 0..BitString.MAX_LENGTH) {
            throw BitStringOverflowException()
        }

        @JvmStatic
        protected fun get(bytes: ByteArray, index: Int): Boolean {
            val byteIndex = index / Byte.SIZE_BITS
            val bitMask = (1 shl (7 - (index % Byte.SIZE_BITS))).toByte()
            return (bytes[byteIndex] and bitMask) != 0.toByte()
        }

        private fun bytesSize(bits: Int): Int {
            checkSize(bits)
            return bits / Byte.SIZE_BITS + if (bits % Byte.SIZE_BITS == 0) 0 else 1
        }
    }
}

internal inline val Int.byteIndex get() = this / Byte.SIZE_BITS
internal inline val Int.bitMask get() = (1 shl (7 - (this % Byte.SIZE_BITS))).toByte()
