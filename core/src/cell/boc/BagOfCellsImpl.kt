package org.ton.kotlin.cell.boc

import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import org.ton.kotlin.cell.Cell
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.DataCell

internal data class BagOfCellsImpl(
    override val roots: List<DataCell>
) : BagOfCells, List<Cell> by roots, CellContext by CellContext.EMPTY {
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
