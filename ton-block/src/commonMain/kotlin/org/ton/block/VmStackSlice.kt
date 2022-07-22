package org.ton.block

import org.ton.cell.Cell
import org.ton.cell.CellSlice

sealed interface VmStackSlice : VmStackValue {
    val cell: Cell
    val st_bits: Int
    val end_bits: Int
    val st_ref: Int
    val end_ref: Int

    fun toCellSlice(): CellSlice = cell.beginParse().run {
        skipBits(st_bits)
        loadRefs(st_ref)
        CellSlice.of(
            loadBits(end_bits - st_bits),
            loadRefs(end_ref - st_ref)
        )
    }

    // TODO: Create cell slice reader without object creation overhead
    fun asCellSlice(): CellSlice = toCellSlice()
}