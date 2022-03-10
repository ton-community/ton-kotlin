package ton.bitstring

class BitStringBuilder : BitStringWriter {
    val bits = ArrayList<UByte>()

    private inline val Int.byteIndex get() = this / 8 or 0
    override var writePosition: Int = 0

    override operator fun set(index: Int, value: Boolean) {
        val byteIndex = index.byteIndex
        val appendBits = (byteIndex + 1) - bits.size
        if (appendBits > 0) {
            bits.addAll(UByteArray(appendBits))
        }
        bits[byteIndex] = if (value) {
            bits[byteIndex] or (1 shl 7 - index % 8).toUByte()
        } else {
            bits[byteIndex] and (1 shl 7 - index % 8).inv().toUByte()
        }
    }

    fun toBitString() = BitString(writePosition, bits.toUByteArray())
}

fun buildBitString(builder: BitStringBuilder.() -> Unit): BitString = BitStringBuilder().apply(builder).toBitString()