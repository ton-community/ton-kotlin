package org.ton.block

import kotlinx.serialization.SerialName
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.kotlin.cell.CellSize
import org.ton.kotlin.cell.CellSizeable
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbObject
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.providers.TlbConstructorProvider

@SerialName("anycast_info")

public data class Anycast(
    val depth: Int,
    @SerialName("rewrite_pfx") val rewritePfx: BitString
) : TlbObject, CellSizeable {
    public constructor(
        rewritePfx: BitString
    ) : this(rewritePfx.size, rewritePfx)

    init {
        require(depth in 1..30) { "required: depth in 1..30, actual: $depth" }
    }

    override val cellSize: CellSize get() = CellSize(5 + rewritePfx.size, 0)

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("anycast_info") {
            field("depth", depth)
            field("rewrite_pfx", rewritePfx)
        }
    }

    override fun toString(): String = print().toString()


    public companion object : TlbConstructorProvider<Anycast> by AnycastTlbConstructor
}

private object AnycastTlbConstructor : TlbConstructor<Anycast>(
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
        val rewritePfx = loadBits(depth)
        Anycast(depth, rewritePfx)
    }
}
