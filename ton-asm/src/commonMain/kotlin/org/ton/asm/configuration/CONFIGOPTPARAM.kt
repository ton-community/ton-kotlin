package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CONFIGOPTPARAM : Instruction, TlbConstructorProvider<CONFIGOPTPARAM> by CONFIGOPTPARAMTlbConstructor {
    override fun toString(): String = "CONFIGOPTPARAM"
}

private object CONFIGOPTPARAMTlbConstructor : TlbConstructor<CONFIGOPTPARAM>(
    schema = "asm_configoptparam#fb33 = CONFIGOPTPARAM;",
    type = CONFIGOPTPARAM::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CONFIGOPTPARAM) {
    }

    override fun loadTlb(cellSlice: CellSlice): CONFIGOPTPARAM = CONFIGOPTPARAM
}