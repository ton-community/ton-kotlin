@file:Suppress("NOTHING_TO_INLINE")

package org.ton.bitstring

import kotlinx.serialization.Serializable
import org.ton.bitstring.serialization.HexBitStringSerializer
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

public inline fun BitString(byteArray: ByteArray, size: Int = byteArray.size * Byte.SIZE_BITS): BitString =
    BitString.of(byteArray, size)

public inline fun BitString(size: Int): BitString = BitString.of(size)
public inline fun BitString(vararg bits: Boolean): BitString = BitString.of(*bits)
public inline fun BitString(bits: Iterable<Boolean>): BitString = BitString.of(bits)
public inline fun BitString(bits: Collection<Boolean>): BitString = BitString.of(bits)
public inline fun BitString(hex: String): BitString = BitString.of(hex)

public inline fun Iterable<Boolean>.toBitString(): BitString = BitString(this)
public inline fun BooleanArray.toBitString(): BitString = BitString(*this)
public inline fun ByteArray.toBitString(size: Int = this.size * Byte.SIZE_BITS): BitString = BitString(this, size)

@Serializable(with = HexBitStringSerializer::class)
public interface BitString : Iterable<Boolean>, Comparable<BitString> {
    public val size: Int

    public operator fun get(index: Int): Boolean
    public fun getOrNull(index: Int): Boolean?

    public fun countLeadingBits(fromIndex: Int = 0, toIndex: Int = size, bit: Boolean): Int

    public operator fun plus(bit: Boolean): BitString =
        plus(booleanArrayOf(bit))

    public operator fun plus(bits: BooleanArray): BitString =
        plus(bits.asIterable())

    public operator fun plus(bits: Collection<Boolean>): BitString =
        plus(bits.asIterable())

    public operator fun plus(bits: Iterable<Boolean>): BitString =
        binary(toBinary() + bits.joinToString("") { if (it) "1" else "0" })

    public operator fun plus(bits: BitString): BitString
    public operator fun plus(bytes: ByteArray): BitString = plus(bytes, bytes.size * Byte.SIZE_BITS)
    public fun plus(bytes: ByteArray, bits: Int): BitString

    public fun toByteArray(augment: Boolean = false): ByteArray
    public fun toBooleanArray(): BooleanArray
    public fun toMutableBitString(): MutableBitString
    public fun toBitString(): BitString = this

    public fun startsWith(prefix: BitString): Boolean =
        toBinary().startsWith(prefix.toBinary())

    public fun endsWith(suffix: BitString): Boolean =
        toBinary().endsWith(suffix.toBinary())

    public fun commonPrefixWith(other: BitString): BitString =
        binary(toBinary().commonPrefixWith(other.toBinary()))

    public fun commonSuffixWith(other: BitString): BitString =
        binary(toBinary().commonSuffixWith(other.toBinary()))

    public fun slice(indices: IntRange): BitString = slice(indices.first, indices.last)
    public fun slice(startIndex: Int, endIndex: Int = size): BitString =
        binary(toBinary().substring(startIndex, endIndex))

    public fun copyInto(
        destination: MutableBitString,
        destinationOffset: Int = 0,
        startIndex: Int = 0,
        endIndex: Int = size
    ) {
        val length = endIndex - startIndex
        for (i in 0 until length) {
            destination[destinationOffset + i] = this[startIndex + i]
        }
    }

    public infix fun xor(other: BitString): BitString
    public infix fun or(other: BitString): BitString

    public fun isEmpty(): Boolean = size == 0

    override fun compareTo(other: BitString): Int = toBinary().compareTo(other.toBinary())

    override fun toString(): String

    public fun toBinary(): String = joinToString("") { if (it) "1" else "0" }

    public fun toHexString(): String

    public companion object {
        public val ALL_ZERO: BitString = ByteBackedBitString.of(ByteArray(128), 1023)
        public val ALL_ONE: BitString = ByteBackedBitString.of(ByteArray(128) {
            0xFF.toByte()
        }, 1023)

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
        public fun of(bits: Iterable<Boolean>): BitString =
            of(bits.toList())

        @JvmStatic
        public fun of(bits: Collection<Boolean>): BitString {
            if (bits.isEmpty()) return empty()
            val bitString = ByteBackedMutableBitString.of(bits.size)
            bits.forEachIndexed { index, bit ->
                bitString[index] = bit
            }
            return bitString
        }

        @JvmStatic
        public fun of(hex: String): BitString {
            if (hex.isEmpty()) return empty()
            // True if bit string doesn't contain mod 4 number of bits
            val incomplete = hex.last() == '_'

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
    }
}

@OptIn(ExperimentalContracts::class)
public inline fun BitString?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || this.size == 0
}
