package org.ton.bitstring

import kotlinx.serialization.Serializable
import org.ton.bitstring.exception.BitStringOverflowException
import org.ton.bitstring.exception.BitStringUnderflowException
import org.ton.crypto.hex
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.math.min

fun BitString(length: Int, byteArray: ByteArray): BitString = BitString.of(length, byteArray)
fun BitString(byteArray: ByteArray): BitString = BitString.of(byteArray)
fun BitString(length: Int): BitString = BitString.of(length)
fun BitString(vararg bits: Boolean): BitString = BitString.of(*bits)
fun BitString(bits: Iterable<Boolean>): BitString = BitString.of(bits)
fun BitString(hex: String): BitString = BitString.of(hex)

fun ByteArray.toBitString(length: Int = size * Byte.SIZE_BITS): BitString = BitString(length, this)

@Serializable(with = FiftHexBitStringSerializer::class)
interface BitString : Iterable<Boolean>, Comparable<BitString> {
    val length: Int

    operator fun set(index: Int, bit: Int)
    operator fun set(index: Int, bit: Boolean)
    operator fun get(index: Int): Boolean
    fun getOrNull(index: Int): Boolean?
    operator fun plus(bits: BooleanArray): BitString
    operator fun plus(bits: Iterable<Boolean>): BitString
    operator fun plus(bytes: ByteArray): BitString
    fun slice(indices: IntRange): BitString
    fun toByteArray(): ByteArray
    fun toBooleanArray(): BooleanArray
    fun isEmpty(): Boolean = length == 0

    override fun toString(): String

    class BitStringIterator(
        private val bitString: BitString
    ) : BooleanIterator() {
        private var index = 0
        override fun hasNext(): Boolean = index < bitString.length
        override fun nextBoolean(): Boolean = bitString[index++]
    }

    override fun iterator(): Iterator<Boolean> = BitStringIterator(this)

    companion object {
        const val MAX_LENGTH = 1023
        const val BYTES_SIZE = Byte.SIZE_BYTES * 128

        @JvmStatic
        fun of(length: Int, byteArray: ByteArray): BitString =
            ByteArrayBitStringImpl(length, byteArray)

        @JvmStatic
        fun of(byteArray: ByteArray): BitString =
            ByteArrayBitStringImpl(length = min(MAX_LENGTH, byteArray.size * Byte.SIZE_BITS), bytes = byteArray)

        @JvmStatic
        fun of(length: Int): BitString = ByteArrayBitStringImpl(length)

        @JvmStatic
        fun binary(bits: String): BitString = BitString(bits.map { char -> char == '1' })

        @JvmStatic
        fun of(vararg bits: Boolean): BitString {
            val bitString = ByteArrayBitStringImpl(bits.size)
            bits.forEachIndexed { index, bit ->
                bitString[index] = bit
            }
            return bitString
        }

        @JvmStatic
        fun of(bits: Iterable<Boolean>): BitString {
            val bitsList = bits.toList()
            val bitString = ByteArrayBitStringImpl(bitsList.size)
            bitsList.forEachIndexed { index, bit ->
                bitString[index] = bit
            }
            return bitString
        }

        @JvmStatic
        fun of(hex: String): BitString {
            // True if bit string doesn't contain mod 4 number of bits
            val incomplete = hex.isNotEmpty() && hex.last() == '_'

            val bits = hex.asSequence()
                .takeWhile { it != '_' } // consume entire hexadecimal string, except for `_`
                .map { char ->
                    char.digitToInt(16)
                        .toString(2)
                        .padStart(4, '0')
                        .map { bit -> bit == '1' } // Convert character to bits it represents
                }
                .flatten()
                .toList()
                .dropLastWhile { incomplete && !it } // drop last elements up to first `1`, if incomplete
                .dropLast(if (incomplete) 1 else 0) // if incomplete, drop the 1 as well

            return BitString(bits)
        }
    }
}

internal class ByteArrayBitStringImpl constructor(
    override var length: Int = BitString.MAX_LENGTH,
    bytes: ByteArray = ByteArray(0)
) : BitString {
    init {
        checkLength(length)
        checkLength(bytes)
    }

    var bytes = bytes.copyOf(bytesSize(length))

    override operator fun set(index: Int, bit: Int) = set(index, bit != 0)

    override operator fun set(index: Int, bit: Boolean) {
        val wordIndex = index.byteIndex
        val bitMask = index.bitMask
        if (bit) {
            bytes[wordIndex] = bytes[wordIndex] or bitMask
        } else {
            bytes[wordIndex] = bytes[wordIndex] and bitMask.inv()
        }
    }

    override operator fun get(index: Int): Boolean = getOrNull(index) ?: throw BitStringUnderflowException()

    override fun getOrNull(index: Int): Boolean? {
        return if (index !in 0..length) {
            null
        } else {
            val byteIndex = index / Byte.SIZE_BITS
            val bitMask = (1 shl (7 - (index % Byte.SIZE_BITS))).toByte()
            (bytes[byteIndex] and bitMask) != 0.toByte()
        }
    }

    override fun plus(bits: BooleanArray): BitString = plus(bits.asIterable())
    override fun plus(bytes: ByteArray): BitString = append(bytes, bytes.size * Byte.SIZE_BITS)

    override fun plus(bits: Iterable<Boolean>): BitString = apply {
        if (bits is ByteArrayBitStringImpl) {
            append(bits.bytes, bits.length)
        } else {
            val bitsList = bits.toList()
            val bitsCount = bitsList.size
            checkLength(length + bitsCount)
            length += bitsCount
            if (bitsList.isNotEmpty()) {
                bits.forEachIndexed { index, bit ->
                    set(length + index, bit)
                }
            }
        }
    }

    override fun slice(indices: IntRange): BitString {
        val result = ByteArrayBitStringImpl(length = indices.last - indices.first + 1)
        for ((position, i) in indices.withIndex()) {
            result[position] = get(i)
        }
        return result
    }

    override fun toByteArray(): ByteArray = bytes.copyOf()

    override fun toBooleanArray(): BooleanArray = toList().toBooleanArray()

    override fun compareTo(other: BitString): Int {
        val limit = min(length, other.length)
        repeat(limit) {
            val thisValue = this[it]
            val otherValue = other[it]
            if (thisValue != otherValue) {
                return if (thisValue) {
                    1
                } else {
                    -1
                }
            }
        }
        return length - other.length
    }

    override fun toString(): String {
        if (length == 0) return ""
        val data = appendTag(bytes, length)
        val result = StringBuilder(hex(data))
        when (length % 8) {
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
        if (javaClass != other?.javaClass) return false

        other as BitString

        if (length != other.length) return false
        if (other is ByteArrayBitStringImpl) {
            if (!bytes.contentEquals(other.bytes)) return false
        } else {
            if (!toBooleanArray().contentEquals(other.toBooleanArray())) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = length
        result = 31 * result + bytes.contentHashCode()
        return result
    }

    private fun append(bytes: ByteArray, bits: Int) = apply {
        require(length + bits <= BitString.MAX_LENGTH) { throw BitStringOverflowException() }
        if (bits != 0) {
            if (length % 8 == 0) {
                if (bits % 8 == 0) {
                    appendWithoutShifting(bytes, bits)
                } else {
                    appendWithShifting(bytes, bits)
                }
            } else {
                // TODO:
//                appendWithDoubleShifting(bytes, bits)
                plus(bytes.toBitString().toBooleanArray())
            }
        }
    }

    private fun appendWithoutShifting(bytes: ByteArray, bits: Int) {
        require(length % 8 == 0)
        require(bits % 8 == 0)

        val newBytes = this.bytes.copyOf(bytesSize(length + bits))
        bytes.copyInto(
            destination = newBytes,
            destinationOffset = length / Byte.SIZE_BITS,
            endIndex = bits / Byte.SIZE_BITS
        )
        this.bytes = newBytes
        length += bits
    }

    private fun appendWithShifting(bytes: ByteArray, bits: Int) {
        require(length % 8 == 0)
        val shift = bits % 8
        require(shift != 0)

        val newBytes = this.bytes.copyOf(bytesSize(length + bits))
        bytes.copyInto(
            destination = newBytes,
            destinationOffset = length / Byte.SIZE_BITS,
            endIndex = bits / Byte.SIZE_BITS + 1
        )
        var lastByte = bytes[bits / Byte.SIZE_BITS].toInt()
        lastByte = lastByte shr (8 - shift)
        lastByte = lastByte shl (8 - shift)
        newBytes[(length + bits) / Byte.SIZE_BITS] = lastByte.toByte()
        this.bytes = newBytes
        length += bits
    }

    private fun appendWithDoubleShifting(bytes: ByteArray, bits: Int) {
        TODO()
    }

    private fun checkLength(length: Int) = require(length <= BitString.MAX_LENGTH) {
        throw BitStringOverflowException()
    }

    private fun checkLength(bytes: ByteArray) {
        require(bytes.size <= BitString.BYTES_SIZE) {
            "ByteArray length expected: 0..${BitString.BYTES_SIZE}, actual: ${bytes.size}"
        }
    }
}

private inline val Int.byteIndex get() = this / Byte.SIZE_BITS
private inline val Int.bitMask get() = (1 shl (7 - (this % Byte.SIZE_BITS))).toByte()

private fun bytesSize(bits: Int) = bits / Byte.SIZE_BITS + if (bits % Byte.SIZE_BITS == 0) 0 else 1

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
