package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDSLICEXQ : AsmInstruction, TlbConstructorProvider<PLDSLICEXQ> by PLDSLICEXQTlbConstructor {
    override fun toString(): String = "PLDSLICEXQ"
}

private object PLDSLICEXQTlbConstructor : TlbConstructor<PLDSLICEXQ>(
    schema = "asm_pldslicexq#d71b = PLDSLICEXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDSLICEXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDSLICEXQ = PLDSLICEXQ
}