package org.ton.asm.compareint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LESSINT(
    val yy: Byte
) : AsmInstruction {
    override fun toString(): String = "$yy LESSINT"

    companion object : TlbConstructorProvider<LESSINT> by LESSINTTlbConstructor
}

private object LESSINTTlbConstructor : TlbConstructor<LESSINT>(
    schema = "asm_lessint#c1 yy:int8 = LESSINT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LESSINT) {
        cellBuilder.storeInt(value.yy, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LESSINT {
        val yy = cellSlice.loadInt(8).toByte()
        return LESSINT(yy)
    }
}
