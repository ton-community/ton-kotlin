package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDSAME : AsmInstruction, TlbConstructorProvider<LDSAME> by LDSAMETlbConstructor {
    override fun toString(): String = "LDSAME"
}

private object LDSAMETlbConstructor : TlbConstructor<LDSAME>(
    schema = "asm_ldsame#d762 = LDSAME;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSAME) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDSAME = LDSAME
}