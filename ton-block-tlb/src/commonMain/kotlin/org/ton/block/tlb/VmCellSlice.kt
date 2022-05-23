package org.ton.block.tlb

import org.ton.block.VmCellSlice
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

object VmCellSliceTlbConstructor : TlbConstructor<VmCellSlice>(
    schema = "_ cell:^Cell st_bits:(## 10) end_bits:(## 10) { st_bits <= end_bits } " +
            "st_ref:(#<= 4) end_ref:(#<= 4) { st_ref <= end_ref } = VmCellSlice;"
) {
    override fun encode(
        cellBuilder: CellBuilder,
        value: VmCellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeRef(value.cell)
        storeUInt(value.stBits, 10)
        storeUInt(value.endBits, 10)
        storeUIntLeq(value.stRef, 4)
        storeUIntLeq(value.endRef, 4)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): VmCellSlice = cellSlice {
        val cell = cellSlice.loadRef()
        val stBits = cellSlice.loadUInt(10).toInt()
        val endBits = cellSlice.loadUInt(10).toInt()
        val stRef = cellSlice.loadUInt(4).toInt()
        val endRef = cellSlice.loadUInt(4).toInt()
        VmCellSlice(cell, stBits, endBits, stRef, endRef)
    }
}
