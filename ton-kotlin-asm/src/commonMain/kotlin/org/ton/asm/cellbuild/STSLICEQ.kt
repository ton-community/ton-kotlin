package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STSLICEQ : AsmInstruction, TlbConstructorProvider<STSLICEQ> by STSLICEQTlbConstructor {
    override fun toString(): String = "STSLICEQ"
}

private object STSLICEQTlbConstructor : TlbConstructor<STSLICEQ>(
    schema = "asm_stsliceq#cf1a = STSLICEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STSLICEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STSLICEQ = STSLICEQ
}
