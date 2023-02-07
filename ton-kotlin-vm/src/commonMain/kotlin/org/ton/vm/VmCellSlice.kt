package org.ton.vm

import org.ton.cell.Cell
import org.ton.cell.CellSlice
import kotlin.jvm.JvmStatic

/**
 * ```tl-b
 * _ cell:^Cell st_bits:(## 10) end_bits:(## 10) { st_bits <= end_bits }
 *   st_ref:(#<= 4) end_ref:(#<= 4) { st_ref <= end_ref } = VmCellSlice;
 */
public data class VmCellSlice(
    val data: Cell,
    val stBits: Int,
    val endBits: Int,
    val stRef: Int,
    val endRef: Int
) {
    init {
        require(stBits <= endBits)
        require(stRef <= 4)
        require(endRef <= 4)
        require(stRef <= endRef)
    }

    public fun toCellSlice(): CellSlice {
        return CellSlice(data.bits.slice(stBits, endBits), data.refs.subList(stRef, endRef))
    }

    public companion object {
        @JvmStatic
        public fun fromCellSlice(cellSlice: CellSlice): VmCellSlice =
            VmCellSlice(Cell(cellSlice.bits, cellSlice.refs), 0, cellSlice.bits.size, 0, cellSlice.refs.size)
    }
}
