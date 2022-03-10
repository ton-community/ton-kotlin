package ton.cell

import ton.bitstring.BitString

data class Cell(
    override val bitString: BitString,
    override val cellReferences: Array<Cell>,
) : CellReader {
    override var readPosition: Int = 0
    override var cellReadPosition: Int = 0

    constructor(data: BitString, cellReferences: Iterable<Cell>) : this(data, cellReferences.toList().toTypedArray())

    override fun get(index: Int): Boolean = bitString[index]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Cell

        if (bitString != other.bitString) return false
        if (!cellReferences.contentEquals(other.cellReferences)) return false
        if (readPosition != other.readPosition) return false
        if (cellReadPosition != other.cellReadPosition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bitString.hashCode()
        result = 31 * result + cellReferences.contentHashCode()
        result = 31 * result + readPosition
        result = 31 * result + cellReadPosition
        return result
    }
}