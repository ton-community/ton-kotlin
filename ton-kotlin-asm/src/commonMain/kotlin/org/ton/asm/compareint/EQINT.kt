package org.ton.asm.compareint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class EQINT(
    val yy: Byte
) : AsmInstruction {
    override fun toString(): String = "$yy EQINT"

    companion object : TlbConstructorProvider<EQINT> by EQINTTlbConstructor
}

private object EQINTTlbConstructor : TlbConstructor<EQINT>(
    schema = "asm_eqint#c0 yy:int8 = EQINT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: EQINT) {
        cellBuilder.storeInt(value.yy, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): EQINT {
        val yy = cellSlice.loadInt(8).toByte()
        return EQINT(yy)
    }
}
