package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCUTLAST : Instruction, TlbConstructorProvider<SCUTLAST> by SCUTLASTTlbConstructor {
    override fun toString(): String = "SCUTLAST"
}

private object SCUTLASTTlbConstructor : TlbConstructor<SCUTLAST>(
    schema = "asm_scutlast#d732 = SCUTLAST;",
    type = SCUTLAST::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCUTLAST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCUTLAST = SCUTLAST
}