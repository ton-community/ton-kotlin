package ton.cell

import ton.bitstring.BitString
import ton.bitstring.BitStringReader
import ton.bitstring.buildBitString

interface CellSlice : CellReader {
    val indices: IntRange

    fun readSlice(): CellSlice
}

private class CellSliceImpl(
    override val cell: Cell,
    override val indices: IntRange
) : CellSlice {
    private val bitStringReader = BitStringReader(cell.bitString)

    override val bitString: BitString get() = bitStringReader.bitString
    override val cellReferences: List<Cell> get() = cell.references
    override var cellReadPosition: Int = 0
    override var readPosition: Int = indices.first
    override val remainingBits: Int
        get() = indices.last - readPosition

    override fun get(index: Int): Boolean =
        if (index in indices) {
            bitStringReader[index]
        } else {
            throw IndexOutOfBoundsException("index: $index, indices: $indices")
        }

    override fun readSlice(): CellSlice = readCell().slice()

    override fun toString() = buildBitString {
        indices.forEach { index ->
            writeBit(get(index))
        }
    }.toString()
}

fun Cell.slice(indices: IntRange = 0..this.bitString.size): CellSlice = CellSliceImpl(this, indices)