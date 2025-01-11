package org.ton.boc

import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import org.ton.cell.Cell

internal data class BagOfCellsImpl(
    override val roots: List<Cell>
) : BagOfCells, List<Cell> by roots {
    constructor(root: Cell) : this(roots = listOf(root))

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

    override fun toString(): String = buildString {
        roots.forEach { cell ->
            Cell.toString(cell, this)
        }
    }
}
