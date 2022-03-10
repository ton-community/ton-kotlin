package ton.cell

import ton.bitstring.BitString
import ton.bitstring.BitStringReader

interface CellReader : BitStringReader {
    val cellReferences: Array<Cell>
    var cellReadPosition: Int

    fun readCell() = cellReferences[cellReadPosition++]
}

private data class CellReaderImpl(
    val cell: Cell,
) : CellReader {
    private val bitStringReader = BitStringReader(cell.bitString)

    override val bitString: BitString get() = bitStringReader.bitString
    override val cellReferences: Array<Cell> get() = cell.cellReferences
    override var cellReadPosition: Int = 0
    override var readPosition: Int = 0

    override fun get(index: Int): Boolean = bitStringReader[index]

    override fun toString() =
        "CellReaderImpl(cell=$cell, cellReadPosition=$cellReadPosition, readPosition=$readPosition)"
}

fun CellReader(cell: Cell): CellReader = CellReaderImpl(cell)
fun Cell.reader() = CellReader(this)