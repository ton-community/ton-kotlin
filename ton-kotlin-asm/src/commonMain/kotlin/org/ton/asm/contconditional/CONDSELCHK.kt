package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CONDSELCHK : AsmInstruction, TlbConstructorProvider<CONDSELCHK> by CONDSELCHKTlbConstructor {
    override fun toString(): String = "CONDSELCHK"
}

private object CONDSELCHKTlbConstructor : TlbConstructor<CONDSELCHK>(
    schema = "asm_condselchk#e305 = CONDSELCHK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CONDSELCHK) {
    }

    override fun loadTlb(cellSlice: CellSlice): CONDSELCHK = CONDSELCHK
}
