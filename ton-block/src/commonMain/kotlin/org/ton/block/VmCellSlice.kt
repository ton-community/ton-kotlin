package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.*
import org.ton.tlb.TlbConstructor

@SerialName("vm_stk_cell")
@Serializable
data class VmCellSlice(
    val cell: Cell,
    @SerialName("st_bits")
    val stBits: Int,
    @SerialName("end_bits")
    val endBits: Int,
    @SerialName("st_ref")
    val stRef: Int,
    @SerialName("end_ref")
    val endRef: Int
) {
    constructor(cellSlice: CellSlice) : this(
        cell = Cell(cellSlice.bits, cellSlice.refs),
        stBits = cellSlice.bitsPosition,
        endBits = cellSlice.bits.size,
        stRef = cellSlice.refsPosition,
        endRef = cellSlice.refs.size
    )

    fun toCellSlice(): CellSlice = cell.beginParse().apply {
        loadBits(stBits)
        loadRefs(stRef)
    }

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<VmCellSlice> = VmCellSliceTlbConstructor
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

