@file:Suppress("OPT_IN_USAGE")

package org.ton.bitstring

import kotlinx.serialization.Serializable
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.math.min

@Serializable(with = FiftHexBitStringSerializer::class)
class BitString constructor(
    val length: Int = MAX_LENGTH,
    private val words: ByteArray = ByteArray(length / Byte.SIZE_BITS + if (length % Byte.SIZE_BITS == 0) 0 else 1)
) : Iterable<Boolean>, Comparable<BitString> {

    init {
        require(length <= MAX_LENGTH) { "BitString expected max length: $MAX_LENGTH, actual: $length" }
    }

    constructor(byteArray: ByteArray) : this(byteArray.size * Byte.SIZE_BITS, byteArray)

    operator fun set(index: Int, bit: Int) = set(index, bit != 0)

    operator fun set(index: Int, bit: Boolean) {
        val wordIndex = index.wordIndex
        val bitMask = index.bitMask
        if (bit) {
            words[wordIndex] = words[wordIndex] or bitMask
        } else {
            words[wordIndex] = words[wordIndex] and bitMask.inv()
        }
    }

    operator fun get(index: Int): Boolean {
        val wordIndex = index.wordIndex
        val bitMask = index.bitMask
        return (words[wordIndex] and bitMask) != 0.toByte()
    }

    fun toByteArray(): ByteArray = words.copyOf()

    override fun iterator(): Iterator<Boolean> = BitStringIterator(this)

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

    override fun toString(): String = buildString {
        this@BitString.forEach { bit ->
            if (bit) {
                append('1')
            } else {
                append('0')
            }
        }
    }

    fun toFiftHex(): String {
        val stringBuilder = StringBuilder()

        val l = length % 4
        if (l == 0) {
            words.forEach { byte ->
                val hex = (byte.toInt() and 0xFF).toString(16).uppercase()
                stringBuilder.append(hex)
            }
            if (length % 8 != 0) {
                stringBuilder.setLength(stringBuilder.length - 1)
            }
        } else {
            words.forEach { byte ->
                val hex = (byte.toInt() and 0xFF).toString(16).uppercase()
                stringBuilder.append(hex)
            }
            val bitMask = length.bitMask
            val value = words.last() or bitMask
            val hex = (value.toInt() and 0xFF).toString(16)
            if (bitMask > 0b0000_1111.toByte()) {
                val char = hex.first().uppercaseChar()
                stringBuilder[stringBuilder.lastIndex - 1] = char
                stringBuilder[stringBuilder.lastIndex] = '_'
            } else {
                val char = hex.last().uppercaseChar()
                stringBuilder[stringBuilder.lastIndex] = char
                stringBuilder.append('_')
            }
        }
        return stringBuilder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BitString

        if (length != other.length) return false
        if (!words.contentEquals(other.words)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = length
        result = 31 * result + words.contentHashCode()
        return result
    }

    class BitStringIterator(val bitString: BitString) : BooleanIterator() {
        private var index = 0
        override fun hasNext(): Boolean = index < bitString.length
        override fun nextBoolean(): Boolean = bitString[index++]
    }

    companion object {
        const val MAX_LENGTH = 1023

        fun of(vararg bits: Boolean): BitString {
            val bitString = BitString(bits.size)
            bits.forEachIndexed { index, bit ->
                bitString[index] = bit
            }
            return bitString
        }

        fun of(fiftHex: String): BitString {
            TODO()
        }
    }
}

fun BitString(vararg bits: Boolean): BitString = BitString.of(*bits)
fun BitString(fiftHex: String): BitString = BitString.of(fiftHex)

private inline val Int.wordIndex get() = (this / Byte.SIZE_BITS) or 0
private inline val Int.bitMask get() = (1 shl (7 - (this % Byte.SIZE_BITS))).toByte()
