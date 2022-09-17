package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class CALLXARGS_VAR(
    val p: UByte
) : AsmInstruction {
    override fun toString(): String = "${p-1u} CALLXARGS"

    companion object : TlbConstructorProvider<CALLXARGS_VAR> by CALLXARGS_VARTlbConstructor
}

private object CALLXARGS_VARTlbConstructor : TlbConstructor<CALLXARGS_VAR>(
    schema = "asm_callxargs_var#db0 p:uint4 = CALLXARGS_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CALLXARGS_VAR) {
        cellBuilder.storeUInt(value.p, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): CALLXARGS_VAR {
        val p = cellSlice.loadUInt(4).toUByte()
        return CALLXARGS_VAR(p)
    }
}
