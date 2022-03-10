package ton.cell

import ton.bitstring.BitStringReader

interface CellReader : BitStringReader {
    val cellReferences: Array<Cell>
    var cellReadPosition: Int

    fun readCell() = cellReferences[cellReadPosition++]
}