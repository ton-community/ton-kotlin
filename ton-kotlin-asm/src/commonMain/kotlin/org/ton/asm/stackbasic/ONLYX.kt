package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ONLYX : AsmInstruction, TlbConstructorProvider<ONLYX> by ONLYXTlbConstructor {
    override fun toString(): String = "ONLYX"
}

private object ONLYXTlbConstructor : TlbConstructor<ONLYX>(
    schema = "asm_onlyx#6b = ONLYX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ONLYX) {
    }

    override fun loadTlb(cellSlice: CellSlice): ONLYX = ONLYX
}