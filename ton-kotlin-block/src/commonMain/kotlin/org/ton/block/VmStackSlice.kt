package org.ton.block

import org.ton.cell.Cell
import org.ton.cell.CellSlice

sealed interface VmStackSlice : VmStackValue {
    val cell: Cell
    val stBits: Int
    val endBits: Int
    val stRef: Int
    val endRef: Int

    fun toCellSlice(): CellSlice =
        CellSlice(
            bits = cell.bits.slice(stBits until endBits),
            refs = cell.refs.slice(stRef until endRef)
        )
}
