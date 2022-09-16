package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDSLICEXQ : AsmInstruction, TlbConstructorProvider<LDSLICEXQ> by LDSLICEXQTlbConstructor {
    override fun toString(): String = "LDSLICEXQ"
}

private object LDSLICEXQTlbConstructor : TlbConstructor<LDSLICEXQ>(
    schema = "asm_ldslicexq#d71a = LDSLICEXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSLICEXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDSLICEXQ = LDSLICEXQ
}