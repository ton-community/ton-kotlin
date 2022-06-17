@file:Suppress("NOTHING_TO_INLINE")

package org.ton.bitstring

import kotlinx.serialization.Serializable

inline fun BitString(byteArray: ByteArray, size: Int = byteArray.size * Byte.SIZE_BITS): BitString =
    BitString.of(byteArray, size)

inline fun BitString(size: Int): BitString = BitString.of(size)
inline fun BitString(vararg bits: Boolean): BitString = BitString.of(*bits)
inline fun BitString(bits: Iterable<Boolean>): BitString = BitString.of(bits)
inline fun BitString(hex: String): BitString = BitString.of(hex)

inline fun ByteArray.toBitString(size: Int = this.size * Byte.SIZE_BITS): BitString = BitString(this, size)

@Serializable(with = FiftHexBitStringSerializer::class)
interface BitString : List<Boolean>, Comparable<BitString> {
    override val size: Int

    override operator fun get(index: Int): Boolean
    fun getOrNull(index: Int): Boolean?

    operator fun plus(bits: BooleanArray): BitString
    operator fun plus(bits: Iterable<Boolean>): BitString
    operator fun plus(bits: Collection<Boolean>): BitString
    operator fun plus(bytes: ByteArray): BitString
    fun plus(bytes: ByteArray, bits: Int): BitString

    fun slice(indices: IntRange): BitString
    fun toByteArray(): ByteArray
    fun toBooleanArray(): BooleanArray
    fun toMutableBitString(): MutableBitString

    override fun subList(fromIndex: Int, toIndex: Int): BitString

    override fun toString(): String

    companion object {
        const val MAX_LENGTH = 1023

        @JvmStatic
        fun of(byteArray: ByteArray, size: Int = byteArray.size * Byte.SIZE_BITS): BitString =
            ByteBackedBitString.of(byteArray, size)

        @JvmStatic
        fun of(size: Int = 0): BitString = ByteBackedBitString.of(size)

        @JvmStatic
        fun binary(bits: String): BitString = BitString(bits.map { char ->
            when (char) {
                '1' -> true
                '0' -> false
                else -> throw IllegalArgumentException("Invalid bit: `$char`")
            }
        })

        @JvmStatic
        fun of(vararg bits: Boolean): BitString {
            val bitString = ByteBackedMutableBitString.of(bits.size)
            bits.forEachIndexed { index, bit ->
                bitString[index] = bit
            }
            return bitString
        }

        @JvmStatic
        fun of(bits: Iterable<Boolean>): BitString {
            val bitsList = bits.toList()
            val bitString = ByteBackedMutableBitString.of(bitsList.size)
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

        @JvmStatic
        fun appendAugmentTag(data: ByteArray, bits: Int): ByteArray {
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
        fun findAugmentTag(byteArray: ByteArray): Int {
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
