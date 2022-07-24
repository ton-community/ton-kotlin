package org.ton.asm.exception

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROWARGIF(
    val n: Int
) : Instruction {
    override fun toString(): String = "$n THROWARGIF"

    companion object : TlbConstructorProvider<THROWARGIF> by THROWARGIFTlbConstructor
}

private object THROWARGIFTlbConstructor : TlbConstructor<THROWARGIF>(
    schema = "asm_throwargif#f2dc_ n:uint11 = THROWARGIF;",
    type = THROWARGIF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWARGIF) {
        cellBuilder.storeUInt(value.n, 11)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWARGIF {
        val n = cellSlice.loadUInt(11).toInt()
        return THROWARGIF(n)
    }
}