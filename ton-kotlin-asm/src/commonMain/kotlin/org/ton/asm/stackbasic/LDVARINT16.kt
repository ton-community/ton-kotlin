package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDVARINT16 : AsmInstruction, TlbConstructorProvider<LDVARINT16> by LDVARINT16TlbConstructor {
    override fun toString(): String = "LDVARINT16"
}

private object LDVARINT16TlbConstructor : TlbConstructor<LDVARINT16>(
    schema = "asm_ldvarint16#fa01 = LDVARINT16;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDVARINT16) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDVARINT16 = LDVARINT16
}