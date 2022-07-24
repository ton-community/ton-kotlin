package org.ton.asm.exception

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROW(
    val n: Int
) : Instruction {
    override fun toString(): String = "$n THROW"

    companion object : TlbConstructorProvider<THROW> by THROWTlbConstructor
}

private object THROWTlbConstructor : TlbConstructor<THROW>(
    schema = "asm_throw#f2c4_ n:uint11 = THROW;",
    type = THROW::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROW) {
        cellBuilder.storeUInt(value.n, 11)
    }

    override fun loadTlb(cellSlice: CellSlice): THROW {
        val n = cellSlice.loadUInt(11).toInt()
        return THROW(n)
    }
}