package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETLIBCODE : AsmInstruction, TlbConstructorProvider<SETLIBCODE> by SETLIBCODETlbConstructor {
    override fun toString(): String = "SETLIBCODE"
}

private object SETLIBCODETlbConstructor : TlbConstructor<SETLIBCODE>(
    schema = "asm_setlibcode#fb06 = SETLIBCODE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETLIBCODE) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETLIBCODE = SETLIBCODE
}