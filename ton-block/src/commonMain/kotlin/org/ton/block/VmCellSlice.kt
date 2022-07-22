package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

@SerialName("vm_stk_cell")
@Serializable
data class VmCellSlice(
    override val cell: Cell,
    override val st_bits: Int,
    override val end_bits: Int,
    override val st_ref: Int,
    override val end_ref: Int
) : VmStackSlice {
    constructor(cellSlice: CellSlice) : this(
        cell = Cell(cellSlice.bits, cellSlice.refs),
        st_bits = cellSlice.bitsPosition,
        end_bits = cellSlice.bits.size,
        st_ref = cellSlice.refsPosition,
        end_ref = cellSlice.refs.size
    )

    companion object : TlbCodec<VmCellSlice> by VmCellSliceTlbConstructor {
        @JvmStatic
        fun tlbConstructor(): TlbConstructor<VmCellSlice> = VmCellSliceTlbConstructor
    }
}

private object VmCellSliceTlbConstructor : TlbConstructor<VmCellSlice>(
    schema = "_ cell:^Cell st_bits:(## 10) end_bits:(## 10) { st_bits <= end_bits } " +
            "st_ref:(#<= 4) end_ref:(#<= 4) { st_ref <= end_ref } = VmCellSlice;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmCellSlice
    ) = cellBuilder {
        storeRef(value.cell)
        storeUInt(value.st_bits, 10)
        storeUInt(value.end_bits, 10)
        storeUIntLeq(value.st_ref, 4)
        storeUIntLeq(value.end_ref, 4)
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

