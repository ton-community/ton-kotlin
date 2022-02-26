package ton.bitstring

import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.ceil

class BitString(
    val bitSize: Int,
) : Iterable<Boolean> {
    private var position = 0

    val byteSize = ceil(bitSize / 8.0).toInt()
    val array = ByteArray(byteSize)

    operator fun set(index: Int, value: Boolean) {
        if (value) {
            array[index / 8 or 0] = array[index / 8 or 0] or (1 shl 7 - index % 8).toByte()
        } else {
            array[index / 8 or 0] = array[index / 8 or 0] and (1 shl 7 - index % 8).inv().toByte()
        }
    }

    operator fun get(index: Int): Boolean {
        return array[index / 8 or 0] and (1 shl 7 - index % 8).toByte() > 0
    }

    fun writeBit(value: Boolean = true) {
        set(position++, value)
    }

    fun writeBitArray(value: BooleanArray) {
        value.forEach {
            writeBit(it)
        }
    }

    fun writeByte(value: Byte, bitLength: Int = Byte.SIZE_BITS) {
        repeat(bitLength) {
            val mask = 1 shl it
            val bit = (value and mask.toByte()) != 0.toByte()
            writeBit(bit)
        }
    }

    fun writeShort(value: Short, bitLength: Int = Short.SIZE_BITS) {
        repeat(bitLength) {
            val mask = 1 shl it
            val bit = (value and mask.toShort()) != 0.toShort()
            writeBit(bit)
        }
    }

    fun writeInt(value: Int, bitLength: Int = Int.SIZE_BITS) {
        repeat(bitLength) {
            val mask = 1 shl it
            val bit = (value and mask) != 0
            writeBit(bit)
        }
    }

    fun writeLong(value: Long, bitLength: Int = Long.SIZE_BITS) {
        repeat(bitLength) {
            val mask = 1 shl it
            val bit = (value and mask.toLong()) != 0L
            writeBit(bit)
        }
    }

    fun writeBitString(bitString: BitString) {
        bitString.forEach {
            writeBit(it)
        }
    }

    override fun toString(): String = buildString {
        toString(this)
    }

    private fun toString(sb: StringBuilder) {
        if (position % 4 == 0) {
            array.slice(0 until ceil(position / 8.0).toInt()).forEach {
                val hex = it.toUByte().toString(16).uppercase()
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

    inner class BitStringIterator : BooleanIterator() {
        private var index = 0
        override fun hasNext(): Boolean = index < bitSize
        override fun nextBoolean(): Boolean = get(index++)
    }
}