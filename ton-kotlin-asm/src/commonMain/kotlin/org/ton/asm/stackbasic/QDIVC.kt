package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QDIVC : AsmInstruction, TlbConstructorProvider<QDIVC> by QDIVCTlbConstructor {
    override fun toString(): String = "QDIVC"
}

private object QDIVCTlbConstructor : TlbConstructor<QDIVC>(
    schema = "asm_qdivc#b7a906 = QDIVC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QDIVC) {
    }

    override fun loadTlb(cellSlice: CellSlice): QDIVC = QDIVC
}