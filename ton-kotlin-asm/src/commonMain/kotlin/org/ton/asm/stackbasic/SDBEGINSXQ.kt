package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDBEGINSXQ : AsmInstruction, TlbConstructorProvider<SDBEGINSXQ> by SDBEGINSXQTlbConstructor {
    override fun toString(): String = "SDBEGINSXQ"
}

private object SDBEGINSXQTlbConstructor : TlbConstructor<SDBEGINSXQ>(
    schema = "asm_sdbeginsxq#d727 = SDBEGINSXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDBEGINSXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDBEGINSXQ = SDBEGINSXQ
}