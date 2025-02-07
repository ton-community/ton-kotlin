package org.ton.kotlin.message.address

import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.bitstring.ByteBackedMutableBitString
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer

/**
 * Anycast prefix info.
 *
 * ```tlb
 * anycast_info$_ depth:(#<= 30) { depth >= 1 } rewrite_pfx:(bits depth) = Anycast;
 * ```
 */
public data class Anycast(
    val depth: Int,
    val rewritePrefix: BitString
) {
    public fun rewrite(address: BitString): BitString {
        val result = ByteBackedMutableBitString(address.size)
        rewritePrefix.copyInto(result)
        address.copyInto(result, depth)
        return result
    }

    public companion object : CellSerializer<Anycast> by AnycastSerializer
}

private object AnycastSerializer : CellSerializer<Anycast> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): Anycast {
        val depth = slice.loadUInt(5).toInt()
        val rewritePrefix = slice.loadBitString(depth)
        return Anycast(depth, rewritePrefix)
    }

    override fun store(
        builder: CellBuilder,
        value: Anycast,
        context: CellContext
    ) {
        builder.storeUInt(value.depth.toUInt(), 5)
        builder.storeBitString(value.rewritePrefix)
    }
}