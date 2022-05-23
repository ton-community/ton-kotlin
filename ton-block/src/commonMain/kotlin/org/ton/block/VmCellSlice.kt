package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell

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
)
