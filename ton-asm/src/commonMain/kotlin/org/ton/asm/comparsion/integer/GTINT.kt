package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class GTINT(
    val y: Int
) : Instruction, TlbConstructorProvider<GTINT> by GTINTTlbConstructor {
    override fun toString(): String = "$y GTINT"
}

private object GTINTTlbConstructor : TlbConstructor<GTINT>(
    schema = "asm_gtint#c2 y:int8 = GTINT;",
    type = GTINT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: GTINT) {
        cellBuilder.storeInt(value.y, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): GTINT {
        val y = cellSlice.loadInt(8).toInt()
        return GTINT(y)
    }
}