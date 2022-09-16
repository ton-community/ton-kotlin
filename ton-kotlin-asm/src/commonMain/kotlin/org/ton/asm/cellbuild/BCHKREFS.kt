package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BCHKREFS : AsmInstruction, TlbConstructorProvider<BCHKREFS> by BCHKREFSTlbConstructor {
    override fun toString(): String = "BCHKREFS"
}

private object BCHKREFSTlbConstructor : TlbConstructor<BCHKREFS>(
    schema = "asm_bchkrefs#cf3a = BCHKREFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BCHKREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): BCHKREFS = BCHKREFS
}
