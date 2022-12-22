@file:Suppress("NOTHING_TO_INLINE")

package org.ton.bitstring

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmStatic
import kotlin.math.min

public inline fun BitString(
    byteArray: ByteArray,
    size: Int = byteArray.size * Byte.SIZE_BITS
): BitString = BitString.of(byteArray, size)

public inline fun BitString(size: Int): BitString = BitString.of(size)
public inline fun BitString(vararg bits: Boolean): BitString = BitString.of(*bits)
public inline fun BitString(bits: Iterable<Boolean>): BitString = BitString.of(bits)
public inline fun BitString(hex: String): BitString = BitString.of(hex)

public inline fun Iterable<Boolean>.toBitString(): BitString = BitString(this)
public inline fun BooleanArray.toBitString(): BitString = BitString(*this)
public inline fun ByteArray.toBitString(size: Int = this.size * Byte.SIZE_BITS): BitString = BitString(this, size)

@Serializable(with = FiftHexBitStringSerializer::class)
public interface BitString : Iterable<Boolean>, Comparable<BitString> {
    public val size: Int

    public operator fun get(index: Int): Boolean
    public fun getOrNull(index: Int): Boolean?

    public operator fun plus(bits: BooleanArray): BitString
    public operator fun plus(bits: Iterable<Boolean>): BitString
    public operator fun plus(bits: Collection<Boolean>): BitString
    public operator fun plus(bytes: ByteArray): BitString
    public fun plus(bytes: ByteArray, bits: Int): BitString

    public fun slice(indices: IntRange): BitString
    public fun toByteArray(augment: Boolean = false): ByteArray
    public fun toBooleanArray(): BooleanArray
    public fun toMutableBitString(): MutableBitString

    public infix fun xor(other: BitString): BitString
    public infix fun or(other: BitString): BitString

    override fun toString(): String

    public fun joinToStringBits(): String = joinToString("") { if (it) "1" else "0" }

    override fun compareTo(other: BitString): Int {
        val limit = min(size, other.size)
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
        return size - other.size
    }

    public companion object {
        public const val MAX_LENGTH: Int = 1023

        @JvmStatic
        public fun empty(): BitString = EmptyBitString

        @JvmStatic
        public fun of(byteArray: ByteArray, size: Int = byteArray.size * Byte.SIZE_BITS): BitString =
            ByteBackedBitString.of(byteArray, size)

        @JvmStatic
        public fun of(size: Int = 0): BitString {
            if (size == 0) return empty()
            return ByteBackedBitString.of(size)
        }

        @JvmStatic
        public fun binary(bits: String): BitString {
            if (bits.isEmpty()) return empty()
            return BitString(bits.map { char ->
                when (char) {
                    '1' -> true
                    '0' -> false
                    else -> throw IllegalArgumentException("Invalid bit: `$char`")
                }
            })
        }

        @JvmStatic
        public fun of(vararg bits: Boolean): BitString {
            if (bits.isEmpty()) return empty()
            val bitString = ByteBackedMutableBitString.of(bits.size)
            bits.forEachIndexed { index, bit ->
                bitString[index] = bit
            }
            return bitString
        }

        @JvmStatic
        public fun of(bits: Iterable<Boolean>): BitString {
            val bitsList = bits.toList()
            if (bitsList.isEmpty()) return empty()
            val bitString = ByteBackedMutableBitString.of(bitsList.size)
            bitsList.forEachIndexed { index, bit ->
                bitString[index] = bit
            }
            return bitString
        }

        @JvmStatic
        public fun of(hex: String): BitString {
            if (hex.isEmpty()) return empty()
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
                .toMutableList()
                .dropLastWhile { incomplete && !it } // drop last elements up to first `1`, if incomplete
                .dropLast(if (incomplete) 1 else 0) // if incomplete, drop the 1 as well

            return BitString(bits)
        }

        @JvmStatic
        public fun appendAugmentTag(data: ByteArray, bits: Int): ByteArray {
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

        @JvmStatic
        public fun findAugmentTag(byteArray: ByteArray): Int {
            if (byteArray.isEmpty()) return 0
            var length = byteArray.size * 8
            var index = byteArray.lastIndex
            while (true) {
                val byte = byteArray[index--].toInt()
                if (byte == 0) {
                    length -= 8
                } else {
                    var skip = 1
                    var mask = 1
                    while (byte and mask == 0) {
                        skip++
                        mask = mask shl 1
                    }
                    length -= skip
                    break
                }
            }
            return length
        }
    }
}
