package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CONFIGOPTPARAM : AsmInstruction, TlbConstructorProvider<CONFIGOPTPARAM> by CONFIGOPTPARAMTlbConstructor {
    override fun toString(): String = "CONFIGOPTPARAM"
}

private object CONFIGOPTPARAMTlbConstructor : TlbConstructor<CONFIGOPTPARAM>(
    schema = "asm_configoptparam#f833 = CONFIGOPTPARAM;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CONFIGOPTPARAM) {
    }

    override fun loadTlb(cellSlice: CellSlice): CONFIGOPTPARAM = CONFIGOPTPARAM
}