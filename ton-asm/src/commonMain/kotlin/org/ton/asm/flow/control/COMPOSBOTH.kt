package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object COMPOSBOTH : Instruction, TlbConstructorProvider<COMPOSBOTH> by COMPOSBOTHTlbConstructor {
    override fun toString(): String = "COMPOSBOTH"
}

private object COMPOSBOTHTlbConstructor : TlbConstructor<COMPOSBOTH>(
    schema = "asm_composboth#edf2 = COMPOSBOTH;",
    type = COMPOSBOTH::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: COMPOSBOTH) {
    }

    override fun loadTlb(cellSlice: CellSlice): COMPOSBOTH = COMPOSBOTH
}