package org.ton.asm.exception

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROWARG(
    val n: Int
) : Instruction {
    override fun toString(): String = "$n THROWARG"

    companion object : TlbConstructorProvider<THROWARG> by THROWARGTlbConstructor
}

private object THROWARGTlbConstructor : TlbConstructor<THROWARG>(
    schema = "asm_throwarg#f2cc_ n:uint11 = THROWARG;",
    type = THROWARG::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWARG) {
        cellBuilder.storeUInt(value.n, 11)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWARG {
        val n = cellSlice.loadUInt(11).toInt()
        return THROWARG(n)
    }
}