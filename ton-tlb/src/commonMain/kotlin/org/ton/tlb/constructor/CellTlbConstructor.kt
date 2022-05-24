package org.ton.tlb.constructor

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun Cell.Companion.tlbCodec(): TlbCodec<Cell> = CellTlbConstructor
fun <T : Any> Cell.Companion.tlbCodec(type: TlbCodec<T>): TlbCodec<T> = CellReferencedTlbConstructor(type)

private object CellTlbConstructor : TlbConstructor<Cell>(
    schema = "_ _:Cell = Cell;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: Cell, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeRef(value)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): Cell = cellSlice {
        loadRef()
    }
}

private class CellReferencedTlbConstructor<T : Any>(
    val type: TlbCodec<T>
) : TlbConstructor<T>("") {
    override fun encode(
        cellBuilder: CellBuilder, value: T, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeRef {
            storeTlb(value, type)
        }
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): T = cellSlice {
        loadRef {
            loadTlb(type)
        }
    }
}
