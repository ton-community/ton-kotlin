package org.ton.cell

import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
data class Cell(
    val bitString: BitString,
    val references: List<Cell>,
    val type: CellType = CellType.ORDINARY
) {
    constructor(
        data: String,
        vararg cellReferences: Cell,
        type: CellType = CellType.ORDINARY
    ) : this(
        BitString(data),
        cellReferences.toList(),
        type
    )

    val isExotic: Boolean get() = type.isExotic

    val maxLevel: Int by lazy {
        // TODO: level calculation differ for exotic cells
        references.maxOfOrNull { it.maxLevel } ?: 0
    }

    fun get(index: Int): Boolean = bitString[index]

    fun treeWalk(): Sequence<Cell> = sequence {
        yieldAll(references)
        references.forEach { reference ->
            yieldAll(reference.treeWalk())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Cell

        if (bitString != other.bitString) return false
        if (references != other.references) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bitString.hashCode()
        result = 31 * result + references.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}