package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STVARINT16 : AsmInstruction, TlbConstructorProvider<STVARINT16> by STVARINT16TlbConstructor {
    override fun toString(): String = "STVARINT16"
}

private object STVARINT16TlbConstructor : TlbConstructor<STVARINT16>(
    schema = "asm_stvarint16#fa03 = STVARINT16;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STVARINT16) {
    }

    override fun loadTlb(cellSlice: CellSlice): STVARINT16 = STVARINT16
}