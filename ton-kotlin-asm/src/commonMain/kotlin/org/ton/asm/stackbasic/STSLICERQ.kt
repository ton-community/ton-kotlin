package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STSLICERQ : AsmInstruction, TlbConstructorProvider<STSLICERQ> by STSLICERQTlbConstructor {
    override fun toString(): String = "STSLICERQ"
}

private object STSLICERQTlbConstructor : TlbConstructor<STSLICERQ>(
    schema = "asm_stslicerq#cf1e = STSLICERQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STSLICERQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STSLICERQ = STSLICERQ
}