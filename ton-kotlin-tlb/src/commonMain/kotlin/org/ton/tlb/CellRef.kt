package org.ton.tlb

import org.ton.cell.Cell

inline fun <reified T> CellRef(cell: Cell, codec: TlbCodec<T>): CellRef<T> = CellRef.valueOf(cell, codec)

inline fun <reified T> Cell.asRef(codec: TlbCodec<T>): CellRef<T> = CellRef.valueOf(this, codec)

interface CellRef<T> {
    val cell: Cell
    val codec: TlbCodec<T>
    val value: T

    companion object {
        fun <T> valueOf(cell: Cell, codec: TlbCodec<T>): CellRef<T> = CellRefImpl(cell, codec)
    }
}

private class CellRefImpl<T>(
    override val cell: Cell,
    override val codec: TlbCodec<T>
) : CellRef<T> {
    override val value: T by lazy {
        cell.parse(codec)
    }
}
