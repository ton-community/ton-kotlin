package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

@SerialName("anycast_info")
@Serializable
data class Anycast(
    val depth: Int,
    @SerialName("rewrite_pfx")
    val rewritePfx: BitString
) {
    init {
        require(depth >= 1) { "depth >= 1" }
    }

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCodec<Anycast> = AnycastTlbConstructor()
    }
}

private class AnycastTlbConstructor : TlbConstructor<Anycast>(
    schema = "anycast_info\$_ depth:(#<= 30) { depth >= 1 } rewrite_pfx:(bits depth) = Anycast;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: Anycast
    ) = cellBuilder {
        storeUIntLeq(value.depth, 30)
        storeBits(value.rewritePfx)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Anycast = cellSlice {
        val depth = loadUIntLeq(30).toInt()
        val rewritePfx = loadBitString(depth)
        Anycast(depth, rewritePfx)
    }
}

