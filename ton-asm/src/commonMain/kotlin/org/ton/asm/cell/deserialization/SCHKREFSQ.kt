package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKREFSQ : Instruction, TlbConstructorProvider<SCHKREFSQ> by SCHKREFSQTlbConstructor {
    override fun toString(): String = "SCHKREFSQ"
}

private object SCHKREFSQTlbConstructor : TlbConstructor<SCHKREFSQ>(
    schema = "asm_schkrefsq#d746 = SCHKREFSQ;",
    type = SCHKREFSQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKREFSQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKREFSQ = SCHKREFSQ
}