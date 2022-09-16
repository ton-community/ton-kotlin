package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object REWRITESTDADDR : AsmInstruction, TlbConstructorProvider<REWRITESTDADDR> by REWRITESTDADDRTlbConstructor {
    override fun toString(): String = "REWRITESTDADDR"
}

private object REWRITESTDADDRTlbConstructor : TlbConstructor<REWRITESTDADDR>(
    schema = "asm_rewritestdaddr#fa44 = REWRITESTDADDR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: REWRITESTDADDR) {
    }

    override fun loadTlb(cellSlice: CellSlice): REWRITESTDADDR = REWRITESTDADDR
}