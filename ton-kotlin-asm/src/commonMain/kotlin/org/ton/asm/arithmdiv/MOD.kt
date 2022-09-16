package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MOD : AsmInstruction, TlbConstructorProvider<MOD> by MODTlbConstructor {
    override fun toString(): String = "MOD"
}

private object MODTlbConstructor : TlbConstructor<MOD>(
    schema = "asm_mod#a908 = MOD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MOD) {
    }

    override fun loadTlb(cellSlice: CellSlice): MOD = MOD
}
