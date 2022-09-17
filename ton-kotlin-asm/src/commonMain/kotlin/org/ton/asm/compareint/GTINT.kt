package org.ton.asm.compareint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class GTINT(
    val yy: Byte
) : AsmInstruction {
    override fun toString(): String = "$yy GTINT"

    companion object : TlbConstructorProvider<GTINT> by GTINTTlbConstructor
}

private object GTINTTlbConstructor : TlbConstructor<GTINT>(
    schema = "asm_gtint#c2 yy:int8 = GTINT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: GTINT) {
        cellBuilder.storeInt(value.yy, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): GTINT {
        val yy = cellSlice.loadInt(8).toByte()
        return GTINT(yy)
    }
}
