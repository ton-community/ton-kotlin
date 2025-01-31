package org.ton.boc

import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import org.ton.cell.Cell
import org.ton.cell.DataCell

internal data class BagOfCellsImpl(
    override val roots: List<DataCell>
) : BagOfCells, List<Cell> by roots {
    constructor(root: DataCell) : this(roots = listOf(root))

    override fun iterator(): Iterator<Cell> = iterator {
        yieldAll(roots)
        roots.forEach { root ->
            yieldAll(root.treeWalk())
        }
    }

    override fun toByteArray(): ByteArray {
        val buffer = Buffer()
        buffer.writeBagOfCells(this)
        return buffer.readByteArray()
    }

    override fun toString(): String = "BagOfCellsImpl(roots=$roots)"
}
