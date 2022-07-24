package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDIX : Instruction, TlbConstructorProvider<PLDIX> by PLDIXTlbConstructor {
    override fun toString(): String = "PLDIX"
}

private object PLDIXTlbConstructor : TlbConstructor<PLDIX>(
    schema = "asm_pldix#d702 = PLDIX;",
    type = PLDIX::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDIX) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDIX = PLDIX
}