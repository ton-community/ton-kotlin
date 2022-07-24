package org.ton.asm.exception

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROWIFNOT(
    val n: Int
) : Instruction {
    override fun toString(): String = "$n THROWIFNOT"

    companion object : TlbConstructorProvider<THROWIFNOT> by THROWIFNOTTlbConstructor
}

private object THROWIFNOTTlbConstructor : TlbConstructor<THROWIFNOT>(
    schema = "asm_throwifnot#f2e4_ n:uint11 = THROWIFNOT;",
    type = THROWIFNOT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWIFNOT) {
        cellBuilder.storeUInt(value.n, 11)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWIFNOT {
        val n = cellSlice.loadUInt(11).toInt()
        return THROWIFNOT(n)
    }
}