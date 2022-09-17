package org.ton.asm.constint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHINT_16(
    val xxxx: Short
) : AsmInstruction {
    override fun toString(): String = "$xxxx PUSHINT"

    companion object : TlbConstructorProvider<PUSHINT_16> by PUSHINT_16TlbConstructor
}

private object PUSHINT_16TlbConstructor : TlbConstructor<PUSHINT_16>(
    schema = "asm_pushint_16#81 xxxx:int16 = PUSHINT_16;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHINT_16) {
        cellBuilder.storeInt(value.xxxx, 16)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHINT_16 {
        val xxxx = cellSlice.loadInt(16).toShort()
        return PUSHINT_16(xxxx)
    }
}
