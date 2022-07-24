package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDSLICEX : Instruction, TlbConstructorProvider<PLDSLICEX> by PLDSLICEXTlbConstructor {
    override fun toString(): String = "PLDSLICEX"
}

private object PLDSLICEXTlbConstructor : TlbConstructor<PLDSLICEX>(
    schema = "asm_pldslicex#d719 = PLDSLICEX;",
    type = PLDSLICEX::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDSLICEX) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDSLICEX = PLDSLICEX
}