package org.ton.tlb

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice

inline fun <T> CellRef(cell: Cell, codec: TlbCodec<T>): CellRef<T> = CellRef.valueOf(cell, codec)

inline fun <T> Cell.asRef(codec: TlbCodec<T>): CellRef<T> = CellRef.valueOf(this, codec)

interface CellRef<T> {
    val cell: Cell
    val codec: TlbCodec<T>
    val value: T

    operator fun getValue(thisRef: Any?, property: Any?): T = value

    companion object {
        fun <T> valueOf(cell: Cell, codec: TlbCodec<T>): CellRef<T> = CellRefImpl(cell, codec)

        fun <T> tlbCodec(codec: TlbCodec<T>): TlbCodec<CellRef<T>> = CellRefTlbConstructor(codec)
    }
}

private class CellRefImpl<T>(
    override val cell: Cell,
    override val codec: TlbCodec<T>
) : CellRef<T> {
    override val value: T by lazy(LazyThreadSafetyMode.PUBLICATION) {
        cell.parse(codec)
    }
}

private class CellRefTlbConstructor<T>(
    val codec: TlbCodec<T>
) : TlbCodec<CellRef<T>> {
    override fun storeTlb(cellBuilder: CellBuilder, value: CellRef<T>) {
        cellBuilder.storeRef(value.cell)
    }

    override fun loadTlb(cellSlice: CellSlice): CellRef<T> {
        return cellSlice.loadRef().asRef(codec)
    }
}
