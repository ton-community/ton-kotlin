package ton.types.cell

data class CellBuilder(
    var data: ByteArray = ByteArray(128),
    var cellType: CellType = CellType.Ordinary,
    var lengthInBits: Int = 0,
) {
    fun build(): Cell = Cell(data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CellBuilder

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    fun appendSlice(slice: CellSlice) {
        TODO()
    }

    fun appendRaw(slice: ByteArray, bits: Int) {
        require(slice.size * 8 >= bits)
        require(lengthInBits + bits <= Cell.MAX_DATA_BITS)
        if (bits != 0) {
            if (lengthInBits % 8 == 0) {
                if (bits % 8 == 0) {
                    appendWithoutShifting(slice, bits)
                } else {
                    appendWithSliceShifting(slice, bits)
                }
            } else {
                appendWithDoubleShifting(slice, bits)
            }
        }
    }

    fun appendWithoutShifting(slice: ByteArray, bits: Int) {
        slice.copyInto(data, lengthInBits / 8)
        lengthInBits += bits
    }

    fun appendWithSliceShifting(slice: ByteArray, bits: Int) {
        slice.copyInto(data, lengthInBits / 8)
        lengthInBits += bits
        val sliceShift = bits % 8
        var lastByte = data.last().toInt()
        lastByte = lastByte shr (8 - sliceShift)
        lastByte = lastByte shl (8 - sliceShift)
        data[data.lastIndex] = lastByte.toByte()
    }

    fun appendWithDoubleShifting(slice: ByteArray, bits: Int) {
        TODO()
    }
}
