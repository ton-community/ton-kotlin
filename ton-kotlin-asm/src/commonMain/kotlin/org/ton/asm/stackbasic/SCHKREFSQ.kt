package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKREFSQ : AsmInstruction, TlbConstructorProvider<SCHKREFSQ> by SCHKREFSQTlbConstructor {
    override fun toString(): String = "SCHKREFSQ"
}

private object SCHKREFSQTlbConstructor : TlbConstructor<SCHKREFSQ>(
    schema = "asm_schkrefsq#d746 = SCHKREFSQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKREFSQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKREFSQ = SCHKREFSQ
}