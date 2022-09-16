package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object XCHGX : AsmInstruction, TlbConstructorProvider<XCHGX> by XCHGXTlbConstructor {
    override fun toString(): String = "XCHGX"
}

private object XCHGXTlbConstructor : TlbConstructor<XCHGX>(
    schema = "asm_xchgx#67 = XCHGX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHGX) {
    }

    override fun loadTlb(cellSlice: CellSlice): XCHGX = XCHGX
}