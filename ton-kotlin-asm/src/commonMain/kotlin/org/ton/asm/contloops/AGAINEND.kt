package org.ton.asm.contloops

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object AGAINEND : AsmInstruction, TlbConstructorProvider<AGAINEND> by AGAINENDTlbConstructor {
    override fun toString(): String = "AGAINEND"
}

private object AGAINENDTlbConstructor : TlbConstructor<AGAINEND>(
    schema = "asm_againend#eb = AGAINEND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AGAINEND) {
    }

    override fun loadTlb(cellSlice: CellSlice): AGAINEND = AGAINEND
}
