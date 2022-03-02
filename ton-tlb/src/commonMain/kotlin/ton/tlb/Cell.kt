package ton.tlb

import ton.bitstring.BitString

data class Cell(
    val data: BitString,
    val references: List<Cell> = emptyList(),
) {
    constructor(data: String, vararg references: Cell) : this(BitString(data), references.toList())
    constructor(data: BitString, vararg references: Cell) : this(data, references.toList())
}