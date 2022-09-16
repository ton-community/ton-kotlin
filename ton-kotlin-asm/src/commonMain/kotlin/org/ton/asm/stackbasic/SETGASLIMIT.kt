package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETGASLIMIT : AsmInstruction, TlbConstructorProvider<SETGASLIMIT> by SETGASLIMITTlbConstructor {
    override fun toString(): String = "SETGASLIMIT"
}

private object SETGASLIMITTlbConstructor : TlbConstructor<SETGASLIMIT>(
    schema = "asm_setgaslimit#f801 = SETGASLIMIT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETGASLIMIT) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETGASLIMIT = SETGASLIMIT
}