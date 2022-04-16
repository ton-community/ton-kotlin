package org.ton.cell

import org.ton.bitstring.BitStringReader

interface CellReader : BitStringReader {
    val cell: Cell
    val cellReferences: List<Cell>
    var cellReadPosition: Int

    fun readCell(): CellReader = cellReferences[cellReadPosition++].slice()
}
