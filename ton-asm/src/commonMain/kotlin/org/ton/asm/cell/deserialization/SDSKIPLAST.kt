package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDSKIPLAST : Instruction, TlbConstructorProvider<SDSKIPLAST> by SDSKIPLASTTlbConstructor {
    override fun toString(): String = "SDSKIPLAST"
}

private object SDSKIPLASTTlbConstructor : TlbConstructor<SDSKIPLAST>(
    schema = "asm_sdskiplast#d723 = SDSKIPLAST;",
    type = SDSKIPLAST::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDSKIPLAST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDSKIPLAST = SDSKIPLAST
}