package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CHKNAN : AsmInstruction, TlbConstructorProvider<CHKNAN> by CHKNANTlbConstructor {
    override fun toString(): String = "CHKNAN"
}

private object CHKNANTlbConstructor : TlbConstructor<CHKNAN>(
    schema = "asm_chknan#c5 = CHKNAN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CHKNAN) {
    }

    override fun loadTlb(cellSlice: CellSlice): CHKNAN = CHKNAN
}