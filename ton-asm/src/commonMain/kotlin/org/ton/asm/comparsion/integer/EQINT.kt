package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class EQINT(
    val y: Int
) : Instruction {
    override fun toString(): String = "$y EQINT"

    companion object : TlbConstructorProvider<EQINT> by EQINTTlbConstructor
}

private object EQINTTlbConstructor : TlbConstructor<EQINT>(
    schema = "asm_eqint#c0 y:int8 = EQINT;",
    type = EQINT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: EQINT) {
        cellBuilder.storeInt(value.y, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): EQINT {
        val y = cellSlice.loadTinyInt(8).toInt()
        return EQINT(y)
    }
}