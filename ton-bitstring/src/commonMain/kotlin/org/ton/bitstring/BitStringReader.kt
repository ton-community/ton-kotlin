package org.ton.bitstring

import org.ton.primitives.BigInt
import org.ton.primitives.plus
import org.ton.primitives.times
import org.ton.primitives.toBigInt

interface BitStringReader {
    val remaining: Int
    val bitString: BitString
    var offset: Int

    operator fun get(index: Int): Boolean = bitString[offset]

    fun readBit(): Boolean = get(offset++)

    fun readInt(length: Int): BigInt
    fun readUInt(length: Int): BigInt
    fun readBits(length: Int): BitString

    companion object {
        @JvmStatic
        fun of(bitString: BitString): BitStringReader = BitStringReader(bitString)
    }
}

fun BitStringReader(bitString: BitString): BitStringReader = BitStringReaderImpl(bitString)

private class BitStringReaderImpl(
    override val bitString: BitString,
    override var offset: Int = 0
) : BitStringReader {
    override val remaining: Int get() = bitString.length - offset

    override fun readInt(length: Int): BigInt {
        val isNegative = readBit()
        val int = readUInt(length - 1)
        return if (isNegative) int * -1 else int
    }

    override fun readUInt(length: Int): BigInt {
        require(length in 0..256) { "invalid integer length, expected: 0..256, actual: $length" }
        return if (length <= Long.SIZE_BITS) {
            var value = 0L
            for (i in 0 until length) {
                value *= 2
                value += if (readBit()) 1 else 0
            }
            value.toBigInt()
        } else {
            var value = BigInt(0L)
            for (i in 0 until length) {
                value *= 2
                value += if (readBit()) 1 else 0
            }
            value
        }
    }

    override fun readBits(length: Int): BitString {
        val bits = BooleanArray(length) { readBit() }
        return BitString(*bits)
    }
}
