package org.ton.asm.compareint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class NEQINT(
    val yy: Byte
) : AsmInstruction {
    override fun toString(): String = "$yy NEQINT"

    companion object : TlbConstructorProvider<NEQINT> by NEQINTTlbConstructor
}

private object NEQINTTlbConstructor : TlbConstructor<NEQINT>(
    schema = "asm_neqint#c3 yy:int8 = NEQINT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NEQINT) {
        cellBuilder.storeInt(value.yy, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): NEQINT {
        val yy = cellSlice.loadInt(8).toByte()
        return NEQINT(yy)
    }
}
