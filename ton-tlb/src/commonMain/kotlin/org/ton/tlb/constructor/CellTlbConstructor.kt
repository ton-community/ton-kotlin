package org.ton.tlb.constructor

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

fun Cell.Companion.tlbCodec(): TlbCodec<Cell> = CellTlbConstructor

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
