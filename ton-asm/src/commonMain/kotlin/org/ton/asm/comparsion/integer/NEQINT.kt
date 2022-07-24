package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class NEQINT(
    val y: Int
) : Instruction {
    override fun toString(): String = "$y NEQINT"

    companion object : TlbConstructorProvider<NEQINT> by NEQINTTlbConstructor
}

private object NEQINTTlbConstructor : TlbConstructor<NEQINT>(
    schema = "asm_neqint#c3 y:int8 = NEQINT;",
    type = NEQINT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NEQINT) {
        cellBuilder.storeInt(value.y, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): NEQINT {
        val y = cellSlice.loadInt(8).toInt()
        return NEQINT(y)
    }
}