package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUShort
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROWIF(
    val n: UShort
) : AsmInstruction {
    override fun toString(): String = "$n THROWIF"

    companion object : TlbConstructorProvider<THROWIF> by THROWIFTlbConstructor
}

private object THROWIFTlbConstructor : TlbConstructor<THROWIF>(
    schema = "asm_throwif#f2d4_ n:uint11 = THROWIF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWIF) {
        cellBuilder.storeUInt(value.n, 11)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWIF {
        val n = cellSlice.loadUInt(11).toUShort()
        return THROWIF(n)
    }
}
