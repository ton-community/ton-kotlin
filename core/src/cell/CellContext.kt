package org.ton.cell

public interface CellContext {
    public fun loadCell(cell: Cell): DataCell

    public fun finalizeCell(builder: CellBuilder): Cell

    public companion object {
        public val EMPTY: CellContext = object : CellContext {
            override fun loadCell(cell: Cell): DataCell {
                if (cell is DataCell) return cell
                else throw IllegalArgumentException("Can't load $cell")
            }

            override fun finalizeCell(builder: CellBuilder): Cell {
                return builder.build()
            }
        }
    }
}