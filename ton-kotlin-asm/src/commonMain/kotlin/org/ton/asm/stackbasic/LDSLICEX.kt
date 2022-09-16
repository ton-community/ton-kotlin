package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDSLICEX : AsmInstruction, TlbConstructorProvider<LDSLICEX> by LDSLICEXTlbConstructor {
    override fun toString(): String = "LDSLICEX"
}

private object LDSLICEXTlbConstructor : TlbConstructor<LDSLICEX>(
    schema = "asm_ldslicex#d718 = LDSLICEX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSLICEX) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDSLICEX = LDSLICEX
}