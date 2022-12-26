package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFBITJMP(
    val n: UByte
) : AsmInstruction {
    override fun toString(): String = "$n IFBITJMP"

    companion object : TlbConstructorProvider<IFBITJMP> by IFBITJMPTlbConstructor
}

private object IFBITJMPTlbConstructor : TlbConstructor<IFBITJMP>(
    schema = "asm_ifbitjmp#e39_ n:uint5 = IFBITJMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFBITJMP) {
        cellBuilder.storeUInt(value.n, 5)
    }

    override fun loadTlb(cellSlice: CellSlice): IFBITJMP {
        val n = cellSlice.loadUInt(5).toUByte()
        return IFBITJMP(n)
    }
}
