package org.ton.tlb.constructor

import org.ton.cell.*
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun Cell.Companion.tlbCodec(): TlbCodec<Cell> = CellTlbConstructor
fun <T> Cell.Companion.tlbCodec(type: TlbCodec<T>): TlbCodec<T> = CellReferencedTlbConstructor(type)

private object CellTlbConstructor : TlbConstructor<Cell>(
    schema = "_ _:Cell = Cell;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: Cell
    ) = cellBuilder {
        storeRef(value)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Cell = cellSlice {
        loadRef()
    }
}

private class CellReferencedTlbConstructor<T>(
    val type: TlbCodec<T>
) : TlbConstructor<T>("") {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: T
    ) = cellBuilder {
        storeRef {
            storeTlb(type, value)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): T = cellSlice {
        loadRef {
            loadTlb(type)
        }
    }
}
