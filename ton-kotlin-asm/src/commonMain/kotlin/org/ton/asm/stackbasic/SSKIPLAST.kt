package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SSKIPLAST : AsmInstruction, TlbConstructorProvider<SSKIPLAST> by SSKIPLASTTlbConstructor {
    override fun toString(): String = "SSKIPLAST"
}

private object SSKIPLASTTlbConstructor : TlbConstructor<SSKIPLAST>(
    schema = "asm_sskiplast#d733 = SSKIPLAST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SSKIPLAST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SSKIPLAST = SSKIPLAST
}