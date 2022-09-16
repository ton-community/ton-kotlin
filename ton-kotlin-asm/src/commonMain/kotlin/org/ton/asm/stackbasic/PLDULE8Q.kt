package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDULE8Q : AsmInstruction, TlbConstructorProvider<PLDULE8Q> by PLDULE8QTlbConstructor {
    override fun toString(): String = "PLDULE8Q"
}

private object PLDULE8QTlbConstructor : TlbConstructor<PLDULE8Q>(
    schema = "asm_pldule8q#d75f = PLDULE8Q;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDULE8Q) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDULE8Q = PLDULE8Q
}