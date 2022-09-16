package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDSLICEX : AsmInstruction, TlbConstructorProvider<PLDSLICEX> by PLDSLICEXTlbConstructor {
    override fun toString(): String = "PLDSLICEX"
}

private object PLDSLICEXTlbConstructor : TlbConstructor<PLDSLICEX>(
    schema = "asm_pldslicex#d719 = PLDSLICEX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDSLICEX) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDSLICEX = PLDSLICEX
}