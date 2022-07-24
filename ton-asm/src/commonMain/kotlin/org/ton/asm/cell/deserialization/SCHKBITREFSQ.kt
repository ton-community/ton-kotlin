package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKBITREFSQ : Instruction, TlbConstructorProvider<SCHKBITREFSQ> by SCHKBITREFSQTlbConstructor {
    override fun toString(): String = "SCHKBITREFSQ"
}

private object SCHKBITREFSQTlbConstructor : TlbConstructor<SCHKBITREFSQ>(
    schema = "asm_schkbitrefsq#d747 = SCHKBITREFSQ;",
    type = SCHKBITREFSQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKBITREFSQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKBITREFSQ = SCHKBITREFSQ
}