@file:OptIn(ExperimentalUnsignedTypes::class)

package ton.bitstring

import kotlinx.serialization.Serializable
import kotlin.math.ceil
import kotlin.math.min

@Serializable(with = BitStringSerializer::class)
data class BitString constructor(
    val size: Int,
    val bits: UByteArray,
) : Iterable<Boolean>, Comparable<BitString> {
    private inline val Int.byteIndex get() = this / 8 or 0

    operator fun set(index: Int, value: Boolean) {
        bits[index.byteIndex] = if (value) {
            bits[index.byteIndex] or (1 shl 7 - index % 8).toUByte()
        } else {
            bits[index.byteIndex] and (1 shl 7 - index % 8).inv().toUByte()
        }
    }

    operator fun get(index: Int): Boolean = (bits[(index / 8) or 0] and (1 shl (7 - (index % 8))).toUByte()) > 0u

    operator fun plus(other: BitString): BitString = buildBitString {
        writeBitString(this@BitString)
        writeBitString(other)
    }

    operator fun plus(bit: Boolean) = buildBitString {
        writeBitString(this@BitString)
        writeBit(bit)
    }

    override fun toString(): String = toString(false)
    fun toString(debug: Boolean): String = buildString {
        if (debug) {
            append("[")
        }
        toString(this)
        if (debug) {
            append(" ")
            this@BitString.forEachIndexed { index, bit ->
                append(bit.toInt())
            }
            append(']')
        }
    }

    private fun toString(sb: StringBuilder) {
        if (size % 4 == 0) {
            val slice = bits.slice(0 until ceil(size / 8.0).toInt())
            slice.forEach {
                val hex = it.toString(16).uppercase()
                if (hex.length < 2) {
                    sb.append('0')
                }
                sb.append(hex)
            }
            if (size % 8 != 0) {
                sb.setLength(sb.length - 1)
            }
        } else {
            val temp = buildBitString {
                writeBitString(this@BitString)
                writeBit(true)
                while (writePosition % 4 != 0) {
                    writeBit(false)
                }
            }
            temp.toString(sb)
            sb.append('_')
        }
    }

    override fun compareTo(other: BitString): Int {
        val limit = min(size, other.size)
        repeat(limit) {
            val thisValue = this[it]
            val otherValue = other[it]
            if (thisValue != otherValue) {
                if (thisValue) {
                    return 1
                }
                if (otherValue) {
                    return -1
                }
            }
        }
        return size-other.size
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BitString

        if (size != other.size) return false
        if (!bits.contentEquals(other.bits)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size
        result = 31 * result + bits.contentHashCode()
        return result
    }

    override fun iterator(): Iterator<Boolean> = BitStringIterator()

    inner class BitStringIterator : BooleanIterator() {
        private var index = 0
        override fun hasNext(): Boolean = index < size
        override fun nextBoolean(): Boolean = get(index++)
    }
}

fun BitString(byteArray: ByteArray) = BitString(byteArray.size * Byte.SIZE_BITS, byteArray.toUByteArray())
fun BitString(bitSize: Int) = BitString(bitSize, UByteArray(ceil(bitSize / UByte.SIZE_BITS.toDouble()).toInt()))
fun BitString(vararg bits: Boolean) = buildBitString {
    writeBits(*bits)
}

fun BitString(bitSize: Int, builder: BitString.() -> Unit) = BitString(bitSize).apply(builder)
fun BitString(hex: String) = buildBitString {
    hex.forEach {
        when (it) {
            '0' -> writeUInt(0u, 4)
            '1' -> writeUInt(1u, 4)
            '2' -> writeUInt(2u, 4)
            '3' -> writeUInt(3u, 4)
            '4' -> writeUInt(4u, 4)
            '5' -> writeUInt(5u, 4)
            '6' -> writeUInt(6u, 4)
            '7' -> writeUInt(7u, 4)
            '8' -> writeUInt(8u, 4)
            '9' -> writeUInt(9u, 4)
            'A', 'a' -> writeUInt(10u, 4)
            'B', 'b' -> writeUInt(11u, 4)
            'C', 'c' -> writeUInt(12u, 4)
            'D', 'd' -> writeUInt(13u, 4)
            'E', 'e' -> writeUInt(14u, 4)
            'F', 'f' -> writeUInt(15u, 4)
        }
    }
}

fun Boolean.toInt() = if (this) 1 else 0