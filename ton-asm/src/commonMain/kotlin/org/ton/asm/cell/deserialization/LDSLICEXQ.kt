package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDSLICEXQ : Instruction, TlbConstructorProvider<LDSLICEXQ> by LDSLICEXQTlbConstructor {
    override fun toString(): String = "LDSLICEXQ"
}

private object LDSLICEXQTlbConstructor : TlbConstructor<LDSLICEXQ>(
    schema = "asm_ldslicexq#d71a = LDSLICEXQ;",
    type = LDSLICEXQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSLICEXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDSLICEXQ = LDSLICEXQ
}