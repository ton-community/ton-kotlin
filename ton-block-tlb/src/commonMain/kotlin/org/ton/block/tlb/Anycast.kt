package org.ton.block.tlb

import org.ton.block.Anycast
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

fun Anycast.Companion.tlbCodec(): TlbCodec<Anycast> = AnycastTlbConstructor

private object AnycastTlbConstructor : TlbConstructor<Anycast>(
    schema = "anycast_info\$_ depth:(#<= 30) { depth >= 1 } rewrite_pfx:(bits depth) = Anycast;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: Anycast, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeUIntLeq(value.depth, 30)
        storeBits(value.rewritePfx)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): Anycast = cellSlice {
        val depth = loadUIntLeq(30).toInt()
        val rewritePfx = loadBitString(depth)
        Anycast(depth, rewritePfx)
    }
}
