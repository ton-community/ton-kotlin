package org.ton.asm.appactions

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SENDRAWMSG : AsmInstruction, TlbConstructorProvider<SENDRAWMSG> by SENDRAWMSGTlbConstructor {
    override fun toString(): String = "SENDRAWMSG"
}

private object SENDRAWMSGTlbConstructor : TlbConstructor<SENDRAWMSG>(
    schema = "asm_sendrawmsg#fb00 = SENDRAWMSG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SENDRAWMSG) {
    }

    override fun loadTlb(cellSlice: CellSlice): SENDRAWMSG = SENDRAWMSG
}
