package ton.bitstring

import kotlin.math.pow

interface BitStringWriter {
    var writePosition: Int

    operator fun set(index: Int, value: Boolean)

    fun writeBit(value: Boolean = true) = apply {
        set(writePosition++, value)
    }

    fun writeBits(vararg values: Boolean) = apply {
        values.forEach {
            writeBit(it)
        }
    }

    fun writeInt(value: Int, bitLength: Int = Int.SIZE_BITS) = apply {
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

    fun writeUInt(value: UInt, bitLength: Int = UInt.SIZE_BITS) = apply {
        if (bitLength == 0) {
            if (value == 0u) return this
        }
        for (i in bitLength - 1 downTo 0) {
            val mask = 1u shl i
            val bit = (value and mask) != 0u
            writeBit(bit)
        }
    }

    fun writeBitString(bitString: BitString) = apply {
        bitString.forEach {
            writeBit(it)
        }
    }
}