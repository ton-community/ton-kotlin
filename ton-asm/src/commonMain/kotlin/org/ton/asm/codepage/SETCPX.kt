package org.ton.asm.codepage

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETCPX : Instruction, TlbConstructorProvider<SETCPX> by SETCPXTlbConstructor {
    override fun toString(): String = "SETCPX"
}

private object SETCPXTlbConstructor : TlbConstructor<SETCPX>(
    schema = "asm_setcpx#fff0 = SETCPX;",
    type = SETCPX::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCPX) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETCPX = SETCPX
}