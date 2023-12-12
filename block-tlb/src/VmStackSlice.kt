package org.ton.block

import org.ton.cell.Cell
import org.ton.cell.CellSlice

public sealed interface VmStackSlice : VmStackValue {
    public val cell: Cell
    public val stBits: Int
    public val endBits: Int
    public val stRef: Int
    public val endRef: Int

    public fun toCellSlice(): CellSlice = cell.beginParse().run {
        skipBits(stBits)
        loadRefs(stRef)
        CellSlice.of(
            loadBits(endBits - stBits),
            loadRefs(endRef - stRef)
        )
    }
}
