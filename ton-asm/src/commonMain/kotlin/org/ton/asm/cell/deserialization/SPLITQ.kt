package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SPLITQ : Instruction, TlbConstructorProvider<SPLITQ> by SPLITQTlbConstructor {
    override fun toString(): String = "SPLITQ"
}

private object SPLITQTlbConstructor : TlbConstructor<SPLITQ>(
    schema = "asm_splitq#d737 = SPLITQ;",
    type = SPLITQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SPLITQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SPLITQ = SPLITQ
}