package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NIP : AsmInstruction, TlbConstructorProvider<NIP> by NIPTlbConstructor {
    override fun toString(): String = "NIP"
}

private object NIPTlbConstructor : TlbConstructor<NIP>(
    schema = "asm_nip#31 = NIP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NIP) {
    }

    override fun loadTlb(cellSlice: CellSlice): NIP = NIP
}
