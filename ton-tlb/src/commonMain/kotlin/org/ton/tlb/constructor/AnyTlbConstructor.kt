package org.ton.tlb.constructor

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec

object AnyTlbConstructor : TlbCodec<BitString> {
    override fun storeTlb(cellBuilder: CellBuilder, value: BitString) {
        cellBuilder.storeBits(value)
    }

    override fun loadTlb(cellSlice: CellSlice): BitString {
        return cellSlice.loadBitString(cellSlice.bits.size - cellSlice.bitsPosition)
    }
}
