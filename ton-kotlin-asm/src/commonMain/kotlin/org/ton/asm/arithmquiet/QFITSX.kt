package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QFITSX : AsmInstruction, TlbConstructorProvider<QFITSX> by QFITSXTlbConstructor {
    override fun toString(): String = "QFITSX"
}

private object QFITSXTlbConstructor : TlbConstructor<QFITSX>(
    schema = "asm_qfitsx#b7b600 = QFITSX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QFITSX) {
    }

    override fun loadTlb(cellSlice: CellSlice): QFITSX = QFITSX
}
