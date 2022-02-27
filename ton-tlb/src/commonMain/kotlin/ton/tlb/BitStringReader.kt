package ton.tlb

import ton.bitstring.BitString

data class BitStringReader(
    val bitString: BitString,
    var pos: Int = 0,
) {
    fun readBit(): Boolean {
        return bitString[pos++]
    }

    fun peekBit(): Boolean = bitString[pos]

    fun readInt(bits: Int): Int {
        var int = 0
        for (i in 0 until bits) {
            int *= 2
            int += if (readBit()) 1 else 0
        }
        return int
    }

    fun readBitString(bits: Int): BitString {
        val bs = BitString(bits)
        repeat(bits) {
            val bit = readBit()
            println("READ BIT: $bit")
            bs.writeBit(bit)
        }
        println("bits:$bits - ${bs.bitSize} - " + bs.toString())
        return bs
    }
}

