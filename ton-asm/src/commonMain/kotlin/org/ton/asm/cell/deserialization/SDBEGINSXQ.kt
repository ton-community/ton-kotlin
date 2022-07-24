package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDBEGINSXQ : Instruction, TlbConstructorProvider<SDBEGINSXQ> by SDBEGINSXQTlbConstructor {
    override fun toString(): String = "SDBEGINSXQ"
}

private object SDBEGINSXQTlbConstructor : TlbConstructor<SDBEGINSXQ>(
    schema = "asm_sdbeginsxq#d727 = SDBEGINSXQ;",
    type = SDBEGINSXQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDBEGINSXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDBEGINSXQ = SDBEGINSXQ
}