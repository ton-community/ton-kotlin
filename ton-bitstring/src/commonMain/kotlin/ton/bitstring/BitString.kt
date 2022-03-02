@file:OptIn(ExperimentalUnsignedTypes::class)

package ton.bitstring

import kotlinx.serialization.Serializable
import kotlin.math.ceil
import kotlin.math.pow

@Serializable(with = BitStringSerializer::class)
class BitString constructor(
    val bitSize: Int,
    val array: UByteArray,
) : Iterable<Boolean> {
    private inline val Int.byteIndex get() = this / 8 or 0
    private var position = 0

    operator fun set(index: Int, value: Boolean) {
        array[index.byteIndex] = if (value) {
            array[index.byteIndex] or (1 shl 7 - index % 8).toUByte()
        } else {
            array[index.byteIndex] and (1 shl 7 - index % 8).inv().toUByte()
        }
    }

    operator fun get(index: Int): Boolean = (array[(index / 8) or 0] and (1 shl (7 - (index % 8))).toUByte()) > 0u

    fun writeBit(value: Boolean = true) {
        set(position++, value)
    }

    fun writeBits(vararg values: Boolean) {
        values.forEach {
            writeBit(it)
        }
    }

    fun writeInt(value: Int, bitLength: Int = Int.SIZE_BITS) {
        if (bitLength == 1) {
            when (value) {
                -1 -> writeBit(true)
                0 -> writeBit(false)
                else -> throw IllegalArgumentException("bitLength is too small for $value")
            }
        } else {
            if (value < 0) {
                writeBit(true)
                writeUInt((2.0.pow(bitLength - 1) + value).toUInt(), bitLength - 1)
            } else {
                writeBit(false)
                writeUInt(value.toUInt(), bitLength - 1)
            }
        }
    }

    fun writeUInt(value: UInt, bitLength: Int = UInt.SIZE_BITS) {
        if (bitLength == 0) {
            if (value == 0u) return
        }
        for (i in bitLength - 1 downTo 0) {
            val mask = 1u shl i
            val bit = (value and mask) != 0u
            writeBit(bit)
        }
    }

    fun writeBitString(bitString: BitString) {
        bitString.forEach {
            writeBit(it)
        }
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
        if (position % 4 == 0) {
            val slice = array.slice(0 until ceil(position / 8.0).toInt())
            slice.forEach {
                val hex = it.toString(16).uppercase()
                if (hex.length < 2) {
                    sb.append('0')
                }
                sb.append(hex)
            }
            if (position % 8 != 0) {
                sb.setLength(sb.length - 1)
            }
        } else {
            val temp = copy()
            temp.writeBit(true)
            while (temp.position % 4 != 0) {
                temp.writeBit(false)
            }
            temp.toString(sb)
            sb.append('_')
        }
    }

    fun copy() = BitString(bitSize).also { bitString ->
        bitString.position = position
        array.copyInto(bitString.array)
    }

    override fun iterator(): Iterator<Boolean> = BitStringIterator()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BitString

        if (bitSize != other.bitSize) return false
        if (!array.contentEquals(other.array)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bitSize
        result = 31 * result + array.contentHashCode()
        return result
    }

    inner class BitStringIterator : BooleanIterator() {
        private var index = 0
        override fun hasNext(): Boolean = index < bitSize
        override fun nextBoolean(): Boolean = get(index++)
    }
}


fun BitString(bitSize: Int) = BitString(bitSize, UByteArray(ceil(bitSize / UByte.SIZE_BITS.toDouble()).toInt()))
fun BitString(vararg bits: Boolean) = BitString(bits.size) {
    writeBits(*bits)
}

fun BitString(bitSize: Int, builder: BitString.() -> Unit) = BitString(bitSize).apply(builder)
fun BitString(hex: String) = BitString(hex.length * 4) {
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
            'A' -> writeUInt(10u, 4)
            'B' -> writeUInt(11u, 4)
            'C' -> writeUInt(12u, 4)
            'D' -> writeUInt(13u, 4)
            'E' -> writeUInt(14u, 4)
            'F' -> writeUInt(15u, 4)
        }
    }
}


fun Boolean.toInt() = if (this) 1 else 0