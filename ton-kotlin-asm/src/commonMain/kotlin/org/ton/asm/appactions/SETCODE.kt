package org.ton.asm.appactions

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETCODE : AsmInstruction, TlbConstructorProvider<SETCODE> by SETCODETlbConstructor {
    override fun toString(): String = "SETCODE"
}

private object SETCODETlbConstructor : TlbConstructor<SETCODE>(
    schema = "asm_setcode#fb04 = SETCODE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCODE) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETCODE = SETCODE
}
