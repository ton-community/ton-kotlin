package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object REWRITESTDADDRQ : AsmInstruction, TlbConstructorProvider<REWRITESTDADDRQ> by REWRITESTDADDRQTlbConstructor {
    override fun toString(): String = "REWRITESTDADDRQ"
}

private object REWRITESTDADDRQTlbConstructor : TlbConstructor<REWRITESTDADDRQ>(
    schema = "asm_rewritestdaddrq#fa45 = REWRITESTDADDRQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: REWRITESTDADDRQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): REWRITESTDADDRQ = REWRITESTDADDRQ
}