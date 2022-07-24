package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class RSHIFTBY(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c RSHIFTBY"

    companion object : TlbConstructorProvider<RSHIFTBY> by RSHIFTBYTlbConstructor
}

private object RSHIFTBYTlbConstructor : TlbConstructor<RSHIFTBY>(
    schema = "asm_rshiftby#ab c:uint8 = RSHIFTBY;",
    type = RSHIFTBY::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RSHIFTBY) {
        cellBuilder.storeUInt(value.c, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): RSHIFTBY {
        val c = cellSlice.loadUInt(8).toInt()
        return RSHIFTBY(c)
    }
}
