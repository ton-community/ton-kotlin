package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QRSHIFT : AsmInstruction, TlbConstructorProvider<QRSHIFT> by QRSHIFTTlbConstructor {
    override fun toString(): String = "QRSHIFT"
}

private object QRSHIFTTlbConstructor : TlbConstructor<QRSHIFT>(
    schema = "asm_qrshift#b7ad = QRSHIFT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QRSHIFT) {
    }

    override fun loadTlb(cellSlice: CellSlice): QRSHIFT = QRSHIFT
}