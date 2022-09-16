package org.ton.asm.appconfig

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BALANCE : AsmInstruction, TlbConstructorProvider<BALANCE> by BALANCETlbConstructor {
    override fun toString(): String = "BALANCE"
}

private object BALANCETlbConstructor : TlbConstructor<BALANCE>(
    schema = "asm_balance#f827 = BALANCE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BALANCE) {
    }

    override fun loadTlb(cellSlice: CellSlice): BALANCE = BALANCE
}
