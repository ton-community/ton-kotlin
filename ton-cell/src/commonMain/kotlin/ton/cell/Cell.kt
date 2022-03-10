package ton.cell

import ton.bitstring.BitString

data class Cell(
    val bitString: BitString,
    val cellReferences: Array<Cell>,
) {
    constructor(data: BitString, cellReferences: Iterable<Cell>) : this(data, cellReferences.toList().toTypedArray())

    fun get(index: Int): Boolean = bitString[index]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Cell

        if (bitString != other.bitString) return false
        if (!cellReferences.contentEquals(other.cellReferences)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bitString.hashCode()
        result = 31 * result + cellReferences.contentHashCode()
        return result
    }
}