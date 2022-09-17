package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFNBITJMP(
    val n: UByte
) : AsmInstruction {
    override fun toString(): String = "$n IFNBITJMP"

    companion object : TlbConstructorProvider<IFNBITJMP> by IFNBITJMPTlbConstructor
}

private object IFNBITJMPTlbConstructor : TlbConstructor<IFNBITJMP>(
    schema = "asm_ifnbitjmp#e3b_ n:uint5 = IFNBITJMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNBITJMP) {
        cellBuilder.storeUInt(value.n, 5)
    }

    override fun loadTlb(cellSlice: CellSlice): IFNBITJMP {
        val n = cellSlice.loadUInt(5).toUByte()
        return IFNBITJMP(n)
    }
}
