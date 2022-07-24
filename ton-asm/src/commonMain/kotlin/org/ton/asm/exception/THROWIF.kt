package org.ton.asm.exception

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

data class THROWIF(
    val n: Int
) : Instruction {
    override fun toString(): String = "$n THROWIF"

    companion object : TlbCombinatorProvider<THROWIF> by THROWIFTlbCombinator
}

private object THROWIFTlbCombinator : TlbCombinator<THROWIF>() {
    override val constructors: List<TlbConstructor<out THROWIF>> = listOf(
        THROWIFTinyTlbConstructor, THROWIFTlbConstructor
    )
}

private object THROWIFTinyTlbConstructor : TlbConstructor<THROWIF>(
    schema = "asm_throwif_tiny#f26_ n:uint6 = THROWIF;",
    type = THROWIF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWIF) {
        cellBuilder.storeUInt(value.n, 6)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWIF {
        val n = cellSlice.loadUInt(6).toInt()
        return THROWIF(n)
    }
}

private object THROWIFTlbConstructor : TlbConstructor<THROWIF>(
    schema = "asm_throwif#f2d4_ n:uint11 = THROWIF;",
    type = THROWIF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWIF) {
        cellBuilder.storeUInt(value.n, 11)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWIF {
        val n = cellSlice.loadUInt(11).toInt()
        return THROWIF(n)
    }
}