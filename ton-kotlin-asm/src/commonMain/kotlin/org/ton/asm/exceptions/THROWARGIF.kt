package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUShort
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROWARGIF(
    val n: UShort
) : AsmInstruction {
    override fun toString(): String = "$n THROWARGIF"

    companion object : TlbConstructorProvider<THROWARGIF> by THROWARGIFTlbConstructor
}

private object THROWARGIFTlbConstructor : TlbConstructor<THROWARGIF>(
    schema = "asm_throwargif#f2dc_ n:uint11 = THROWARGIF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWARGIF) {
        cellBuilder.storeUInt(value.n, 11)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWARGIF {
        val n = cellSlice.loadUInt(11).toUShort()
        return THROWARGIF(n)
    }
}
