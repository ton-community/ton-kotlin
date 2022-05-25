package org.ton.block.tlb

import org.ton.block.VmCellSlice
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

fun VmCellSlice.Companion.tlbCodec(): TlbCodec<VmCellSlice> = VmCellSliceTlbConstructor()

private class VmCellSliceTlbConstructor : TlbConstructor<VmCellSlice>(
    schema = "_ cell:^Cell st_bits:(## 10) end_bits:(## 10) { st_bits <= end_bits } " +
            "st_ref:(#<= 4) end_ref:(#<= 4) { st_ref <= end_ref } = VmCellSlice;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmCellSlice
    ) = cellBuilder {
        storeRef(value.cell)
        storeUInt(value.stBits, 10)
        storeUInt(value.endBits, 10)
        storeUIntLeq(value.stRef, 4)
        storeUIntLeq(value.endRef, 4)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmCellSlice = cellSlice {
        val cell = loadRef()
        val stBits = loadUInt(10).toInt()
        val endBits = loadUInt(10).toInt()
        val stRef = loadUIntLeq(4).toInt()
        val endRef = loadUIntLeq(4).toInt()
        VmCellSlice(cell, stBits, endBits, stRef, endRef)
    }
}
