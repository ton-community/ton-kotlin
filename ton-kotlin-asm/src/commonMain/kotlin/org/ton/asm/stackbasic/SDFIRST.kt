package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDFIRST : AsmInstruction, TlbConstructorProvider<SDFIRST> by SDFIRSTTlbConstructor {
    override fun toString(): String = "SDFIRST"
}

private object SDFIRSTTlbConstructor : TlbConstructor<SDFIRST>(
    schema = "asm_sdfirst#c703 = SDFIRST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDFIRST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDFIRST = SDFIRST
}