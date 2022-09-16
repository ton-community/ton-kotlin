package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QPOW2 : AsmInstruction, TlbConstructorProvider<QPOW2> by QPOW2TlbConstructor {
    override fun toString(): String = "QPOW2"
}

private object QPOW2TlbConstructor : TlbConstructor<QPOW2>(
    schema = "asm_qpow2#b7ae = QPOW2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QPOW2) {
    }

    override fun loadTlb(cellSlice: CellSlice): QPOW2 = QPOW2
}