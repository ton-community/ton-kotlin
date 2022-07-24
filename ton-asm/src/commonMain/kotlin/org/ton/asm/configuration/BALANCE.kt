package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BALANCE : Instruction, TlbConstructorProvider<BALANCE> by BALANCETlbConstructor {
    override fun toString(): String = "BALANCE"
}

private object BALANCETlbConstructor : TlbConstructor<BALANCE>(
    schema = "asm_balance#fb27 = BALANCE;",
    type = BALANCE::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BALANCE) {
    }

    override fun loadTlb(cellSlice: CellSlice): BALANCE = BALANCE
}