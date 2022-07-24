package org.ton.asm.flow.conditional

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFNBITJMP(
    val n: Int
) : Instruction {
    override fun toString(): String = "$n IFNBITJMP"

    companion object : TlbConstructorProvider<IFNBITJMP> by IFNBITJMPTlbConstructor
}

private object IFNBITJMPTlbConstructor : TlbConstructor<IFNBITJMP>(
    schema = "asm_ifnbitjmp#e3b_ n:uint32 = IFNBITJMP;",
    type = IFNBITJMP::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNBITJMP) {
        cellBuilder.storeUInt(value.n, 32)
    }

    override fun loadTlb(cellSlice: CellSlice): IFNBITJMP {
        val n = cellSlice.loadUInt(32).toInt()
        return IFNBITJMP(n)
    }
}
