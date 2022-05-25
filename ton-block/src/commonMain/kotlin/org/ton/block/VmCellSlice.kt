package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellSlice

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
        endBits = cellSlice.bits.length,
        stRef = cellSlice.refsPosition,
        endRef = cellSlice.refs.size
    )

    fun toCellSlice(): CellSlice = cell.beginParse().apply {
        loadBits(stBits)
        loadRefs(stRef)
    }
}
