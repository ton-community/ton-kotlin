package org.ton.cell

import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
data class Cell(
    val data: BitString,
    val references: List<Cell> = emptyList(),
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

    fun treeWalk(): Sequence<Cell> = sequence {
        yieldAll(references)
        references.forEach { reference ->
            yieldAll(reference.treeWalk())
        }
    }

    fun beginParse(): CellSlice = CellSlice.beginParse(this)

    /**
     * Computes the representation hash of a cell and returns it as a 256-bit byte array.
     * Useful for signing and checking signatures of arbitrary entities represented by a tree of cells.
     */
    fun hash(): ByteArray {
        TODO()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Cell

        if (data != other.data) return false
        if (references != other.references) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + references.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}