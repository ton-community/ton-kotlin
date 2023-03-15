package org.ton.tlb.constructor

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.buildCell
import org.ton.tlb.TlbCodec

public object AnyTlbConstructor : TlbCodec<Cell> {
    override fun storeTlb(cellBuilder: CellBuilder, value: Cell) {
        cellBuilder.storeBits(value.bits)
        cellBuilder.storeRefs(value.refs)
    }

    override fun loadTlb(cellSlice: CellSlice): Cell {
        return buildCell {
            storeBits(cellSlice.loadBits(cellSlice.bits.size - cellSlice.bitsPosition))
            storeRefs(cellSlice.loadRefs(cellSlice.refs.size - cellSlice.refsPosition))
        }
    }
}
