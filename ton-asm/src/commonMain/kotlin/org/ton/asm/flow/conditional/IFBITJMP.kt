package org.ton.asm.flow.conditional

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFBITJMP(
    val n: Int
) : Instruction {
    override fun toString(): String = "$n IFBITJMP"

    companion object : TlbConstructorProvider<IFBITJMP> by IFBITJMPTlbConstructor
}

private object IFBITJMPTlbConstructor : TlbConstructor<IFBITJMP>(
    schema = "asm_ifbitjmp#e39_ n:uint32 = IFBITJMP;",
    type = IFBITJMP::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFBITJMP) {
        cellBuilder.storeUInt(value.n, 32)
    }

    override fun loadTlb(cellSlice: CellSlice): IFBITJMP {
        val n = cellSlice.loadUInt(32).toInt()
        return IFBITJMP(n)
    }
}