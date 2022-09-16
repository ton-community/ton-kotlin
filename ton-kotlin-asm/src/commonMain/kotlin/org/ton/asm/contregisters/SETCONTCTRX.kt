package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETCONTCTRX : AsmInstruction, TlbConstructorProvider<SETCONTCTRX> by SETCONTCTRXTlbConstructor {
    override fun toString(): String = "SETCONTCTRX"
}

private object SETCONTCTRXTlbConstructor : TlbConstructor<SETCONTCTRX>(
    schema = "asm_setcontctrx#ede2 = SETCONTCTRX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCONTCTRX) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETCONTCTRX = SETCONTCTRX
}
