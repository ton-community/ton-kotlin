package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

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

    override fun toString(): String =
        "(vm_stk_slice cell:${if (st_ref == 0 && end_ref == 0) cell.bits.toString() else cell.toString()} st_bits:$st_bits end_bits:$end_bits st_ref:$st_ref end_ref:$end_ref)"

    companion object : TlbConstructorProvider<VmCellSlice> by VmCellSliceTlbConstructor
}

private object VmCellSliceTlbConstructor : TlbConstructor<VmCellSlice>(
    schema = "vm_stk_slice#04 cell:^Cell st_bits:(## 10) end_bits:(## 10) { st_bits <= end_bits } " +
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

