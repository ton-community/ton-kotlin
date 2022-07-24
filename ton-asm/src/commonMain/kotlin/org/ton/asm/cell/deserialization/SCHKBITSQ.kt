package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKBITSQ : Instruction, TlbConstructorProvider<SCHKBITSQ> by SCHKBITSQTlbConstructor {
    override fun toString(): String = "SCHKBITSQ"
}

private object SCHKBITSQTlbConstructor : TlbConstructor<SCHKBITSQ>(
    schema = "asm_schkbitsq#d745 = SCHKBITSQ;",
    type = SCHKBITSQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKBITSQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKBITSQ = SCHKBITSQ
}