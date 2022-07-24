package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class GETPARAM(
    val i: Int
) : Instruction {
    override fun toString(): String = "$i GETPARAM"

    companion object : TlbConstructorProvider<GETPARAM> by GETPARAMTlbConstructor
}

private object GETPARAMTlbConstructor : TlbConstructor<GETPARAM>(
    schema = "asm_getparam#f82 i:uint4 = GETPARAM;",
    type = GETPARAM::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: GETPARAM) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): GETPARAM {
        val i = cellSlice.loadUInt(4).toInt()
        return GETPARAM(i)
    }
}