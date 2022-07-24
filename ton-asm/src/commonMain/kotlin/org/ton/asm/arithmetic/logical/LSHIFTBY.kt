package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LSHIFTBY(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c LSHIFT#"

    companion object : TlbConstructorProvider<LSHIFTBY> by LSHIFTBYTlbConstructor
}

private object LSHIFTBYTlbConstructor : TlbConstructor<LSHIFTBY>(
    schema = "asm_lshiftby#aa c:uint8 = LSHIFTBY;",
    type = LSHIFTBY::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LSHIFTBY) {
        cellBuilder.storeUInt(value.c, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LSHIFTBY {
        val c = cellSlice.loadUInt(8).toInt()
        return LSHIFTBY(c)
    }
}