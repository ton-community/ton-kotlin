package org.ton.asm.dictsub

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUBDICTURPGET : AsmInstruction, TlbConstructorProvider<SUBDICTURPGET> by SUBDICTURPGETTlbConstructor {
    override fun toString(): String = "SUBDICTURPGET"
}

private object SUBDICTURPGETTlbConstructor : TlbConstructor<SUBDICTURPGET>(
    schema = "asm_subdicturpget#f4b7 = SUBDICTURPGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUBDICTURPGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUBDICTURPGET = SUBDICTURPGET
}
