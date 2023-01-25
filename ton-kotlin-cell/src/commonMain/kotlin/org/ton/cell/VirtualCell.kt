package org.ton.cell

internal class VirtualCell(
    val cell: Cell,
    val offset: Int
) : Cell by cell {
    override val refs: List<Cell>
        get() = cell.refs.map { it.virtualize(offset) }

    override val levelMask: LevelMask
        get() = cell.levelMask.virtualize(offset)

    override fun virtualize(offset: Int): Cell {
        return if (this.offset == offset) this
        else VirtualCell(cell, offset)
    }
}
