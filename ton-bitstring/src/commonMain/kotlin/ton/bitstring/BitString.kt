package ton.bitstring

import kotlinx.serialization.Serializable
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.ceil

@Serializable(with = BitStringSerializer::class)
class BitString(
    val bitSize: Int,
) : Iterable<Boolean> {

    private inline val Int.byteIndex get() = this / 8 or 0
    private var position = 0

    val byteSize = ceil(bitSize / 8.0).toInt()
    val array = ByteArray(byteSize)

    operator fun set(index: Int, value: Boolean) {
        array[index.byteIndex] = if (value) {
            array[index.byteIndex] or (1 shl 7 - index % 8).toByte()
        } else {
            array[index.byteIndex] and (1 shl 7 - index % 8).inv().toByte()
        }
    }

    operator fun get(index: Int): Boolean = array[index.byteIndex] and (1 shl 7 - index % 8).toByte() > 0

    fun writeBit(value: Boolean = true) {
        set(position++, value)
    }

    fun writeBitArray(value: BooleanArray) {
        value.forEach {
            writeBit(it)
        }
    }

    fun writeInt(value: Int, bitLength: Int = Int.SIZE_BITS) {
        writeBit(false)
        writeUInt(value.toUInt(), bitLength - 1)
    }

    fun writeUInt(value: UInt, bitLength: Int = UInt.SIZE_BITS) {
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

fun BitString(vararg boolean: Boolean) = BitString(boolean.size).apply {
    writeBitArray(boolean)
}