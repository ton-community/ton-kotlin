package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object XOR : AsmInstruction, TlbConstructorProvider<XOR> by XORTlbConstructor {
    override fun toString(): String = "XOR"
}

private object XORTlbConstructor : TlbConstructor<XOR>(
    schema = "asm_xor#b2 = XOR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XOR) {
    }

    override fun loadTlb(cellSlice: CellSlice): XOR = XOR
}
