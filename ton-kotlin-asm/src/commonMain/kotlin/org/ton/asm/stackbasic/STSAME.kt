package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STSAME : AsmInstruction, TlbConstructorProvider<STSAME> by STSAMETlbConstructor {
    override fun toString(): String = "STSAME"
}

private object STSAMETlbConstructor : TlbConstructor<STSAME>(
    schema = "asm_stsame#cf42 = STSAME;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STSAME) {
    }

    override fun loadTlb(cellSlice: CellSlice): STSAME = STSAME
}