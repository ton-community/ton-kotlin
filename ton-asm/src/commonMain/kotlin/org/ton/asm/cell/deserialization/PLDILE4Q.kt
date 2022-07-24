package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDILE4Q : Instruction, TlbConstructorProvider<PLDILE4Q> by PLDILE4QTlbConstructor {
    override fun toString(): String = "PLDILE4Q"
}

private object PLDILE4QTlbConstructor : TlbConstructor<PLDILE4Q>(
    schema = "asm_pldile4q#d75c = PLDILE4Q;",
    type = PLDILE4Q::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDILE4Q) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDILE4Q = PLDILE4Q
}