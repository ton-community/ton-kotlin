package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDSLICEX : Instruction, TlbConstructorProvider<LDSLICEX> by LDSLICEXTlbConstructor {
    override fun toString(): String = "LDSLICEX"
}

private object LDSLICEXTlbConstructor : TlbConstructor<LDSLICEX>(
    schema = "asm_ldslicex#d718 = LDSLICEX;",
    type = LDSLICEX::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSLICEX) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDSLICEX = LDSLICEX
}