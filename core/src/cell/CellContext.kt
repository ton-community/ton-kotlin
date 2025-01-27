package org.ton.cell

public interface CellContext {
    public fun loadCell(cell: Cell): Cell

    public fun finalizeCell(builder: CellBuilder): Cell

    public companion object {
        public val EMPTY: CellContext = object : CellContext {
            override fun loadCell(cell: Cell): Cell {
                return cell
            }

            override fun finalizeCell(builder: CellBuilder): Cell {
                return builder.build()
            }
        }
    }
}