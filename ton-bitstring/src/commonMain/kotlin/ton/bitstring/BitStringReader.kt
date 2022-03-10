package ton.bitstring

interface BitStringReader {
    val bitString: BitString
    var readPosition: Int

    operator fun get(index: Int): Boolean

    fun readBit(): Boolean = get(readPosition++)

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

    fun readBitString(bits: Int): BitString = buildBitString {
        repeat(bits) {
            writeBit(readBit())
        }
    }
}

data class BitStringReaderImpl(
    override val bitString: BitString,
    override var readPosition: Int = 0,
) : BitStringReader {
    override fun get(index: Int): Boolean = bitString[index]
}

fun BitStringReader(data: BitString): BitStringReader = BitStringReaderImpl(data)