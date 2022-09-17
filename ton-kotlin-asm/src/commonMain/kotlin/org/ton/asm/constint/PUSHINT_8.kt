package org.ton.asm.constint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHINT_8(
    val xx: Byte
) : AsmInstruction {
    override fun toString(): String = "$xx PUSHINT"

    companion object : TlbConstructorProvider<PUSHINT_8> by PUSHINT_8TlbConstructor
}

private object PUSHINT_8TlbConstructor : TlbConstructor<PUSHINT_8>(
    schema = "asm_pushint_8#80 xx:int8 = PUSHINT_8;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHINT_8) {
        cellBuilder.storeInt(value.xx, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHINT_8 {
        val xx = cellSlice.loadInt(8).toByte()
        return PUSHINT_8(xx)
    }
}
