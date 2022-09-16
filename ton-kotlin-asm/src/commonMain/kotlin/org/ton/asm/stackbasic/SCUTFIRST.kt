package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCUTFIRST : AsmInstruction, TlbConstructorProvider<SCUTFIRST> by SCUTFIRSTTlbConstructor {
    override fun toString(): String = "SCUTFIRST"
}

private object SCUTFIRSTTlbConstructor : TlbConstructor<SCUTFIRST>(
    schema = "asm_scutfirst#d730 = SCUTFIRST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCUTFIRST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCUTFIRST = SCUTFIRST
}