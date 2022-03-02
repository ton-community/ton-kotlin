package ton.bitstring

data class BitStringReader(
    val bitString: BitString,
    var pos: Int = 0,
) {
    fun readBit(): Boolean {
        return bitString[pos++]
    }

    fun peekBit(offset: Int = 0): Boolean = bitString[pos + offset]

    fun skip(bits: Int = 1) {
        pos += bits
    }

    fun readInt(bits: Int = Int.SIZE_BITS): Int {
        val isNegative = readBit()
        val int = readUInt(bits - 1).toInt()
        return if (isNegative) int * -1 else int
    }

    fun readUInt(bits: Int = UInt.SIZE_BITS): UInt {
        var int = 0u
        for (i in 0 until bits) {
            int *= 2u
            int += if (readBit()) 1u else 0u
        }
        return int
    }

    fun readULong(bits: Int = ULong.SIZE_BITS): ULong {
        var long: ULong = 0u
        for (i in 0 until bits) {
            long *= 2u
            long += if (readBit()) 1u else 0u
        }
        return long
    }

    fun readBitString(bits: Int): BitString {
        val bs = BitString(bits)
        repeat(bits) {
            val bit = readBit()
            bs.writeBit(bit)
        }
        return bs
    }

    override fun toString(): String = buildString {
        append("[{")
        append(bitString)
        append(" ")
        bitString.forEachIndexed { index, bit ->
            if (index == pos) {
                append(" HERE->|")
            }
            append(bit.toInt())
        }
        append("}]")
    }
}

