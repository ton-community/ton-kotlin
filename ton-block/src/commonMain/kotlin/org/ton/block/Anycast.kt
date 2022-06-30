package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

@SerialName("anycast_info")
@Serializable
data class Anycast(
    val depth: Int,
    val rewrite_pfx: BitString
) {
    constructor(
        rewrite_pfx: BitString
    ) : this(rewrite_pfx.size, rewrite_pfx)

    init {
        require(depth in 1..30) { "required: depth in 1..30, actual: $depth" }
    }

    override fun toString(): String = "anycast_info(depth:$depth rewrite_pfx:$rewrite_pfx)"

    companion object : TlbCodec<Anycast> by AnycastTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbCodec<Anycast> = AnycastTlbConstructor
    }
}

private object AnycastTlbConstructor : TlbConstructor<Anycast>(
    schema = "anycast_info\$_ depth:(#<= 30) { depth >= 1 } rewrite_pfx:(bits depth) = Anycast;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: Anycast
    ) = cellBuilder {
        storeUIntLeq(value.depth, 30)
        storeBits(value.rewrite_pfx)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Anycast = cellSlice {
        val depth = loadUIntLeq(30).toInt()
        val rewritePfx = loadBits(depth)
        Anycast(depth, rewritePfx)
    }
}

