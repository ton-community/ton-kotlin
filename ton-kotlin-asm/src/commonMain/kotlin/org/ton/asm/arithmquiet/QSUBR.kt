package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QSUBR : AsmInstruction, TlbConstructorProvider<QSUBR> by QSUBRTlbConstructor {
    override fun toString(): String = "QSUBR"
}

private object QSUBRTlbConstructor : TlbConstructor<QSUBR>(
    schema = "asm_qsubr#b7a2 = QSUBR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QSUBR) {
    }

    override fun loadTlb(cellSlice: CellSlice): QSUBR = QSUBR
}
