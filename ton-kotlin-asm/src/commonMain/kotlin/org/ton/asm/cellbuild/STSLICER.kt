package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STSLICER : AsmInstruction, TlbConstructorProvider<STSLICER> by STSLICERTlbConstructor {
    override fun toString(): String = "STSLICER"
}

private object STSLICERTlbConstructor : TlbConstructor<STSLICER>(
    schema = "asm_stslicer#cf16 = STSLICER;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STSLICER) {
    }

    override fun loadTlb(cellSlice: CellSlice): STSLICER = STSLICER
}
