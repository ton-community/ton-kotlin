package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QMULDIVR : AsmInstruction, TlbConstructorProvider<QMULDIVR> by QMULDIVRTlbConstructor {
    override fun toString(): String = "QMULDIVR"
}

private object QMULDIVRTlbConstructor : TlbConstructor<QMULDIVR>(
    schema = "asm_qmuldivr#b7a985 = QMULDIVR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QMULDIVR) {
    }

    override fun loadTlb(cellSlice: CellSlice): QMULDIVR = QMULDIVR
}
