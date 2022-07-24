package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

class LESSINT(
    val y: Int
) : Instruction {
    override fun toString(): String = "$y LESSINT"

    companion object : TlbConstructorProvider<LESSINT> by LESINTTLbConstructor
}

private object LESINTTLbConstructor : TlbConstructor<LESSINT>(
    schema = "asm_lessint#c1 y:int8 = LESSINT;",
    type = LESSINT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LESSINT) {
        cellBuilder.storeInt(value.y, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LESSINT {
        val y = cellSlice.loadInt(8).toInt()
        return LESSINT(y)
    }
}