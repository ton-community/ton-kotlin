package org.ton.cell

import kotlinx.io.bytestring.ByteString

internal class VirtualCell(
    val cell: Cell,
    val offset: Int
) : Cell by cell {
    override val levelMask: LevelMask
        get() = cell.levelMask.virtualize(offset)

    override fun virtualize(offset: Int): Cell {
        return if (this.offset == offset) this
        else VirtualCell(cell, offset)
    }

    override fun hash(level: Int): ByteString = cell.hash(levelMask.apply(level).level)

    override fun depth(level: Int): Int = cell.depth(levelMask.apply(level).level)
}
