package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKREFS : AsmInstruction, TlbConstructorProvider<SCHKREFS> by SCHKREFSTlbConstructor {
    override fun toString(): String = "SCHKREFS"
}

private object SCHKREFSTlbConstructor : TlbConstructor<SCHKREFS>(
    schema = "asm_schkrefs#d742 = SCHKREFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKREFS = SCHKREFS
}
