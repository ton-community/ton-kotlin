package org.ton.block.shard

import org.ton.block.currency.CurrencyCollection
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

/**
 * Intermediate balance info.
 */
public data class DepthBalanceInfo(
    /**
     * Depth for which the balance was calculated.
     */
    val splitDepth: Int,

    /**
     * Total balance for a subtree.
     */
    val balance: CurrencyCollection
) {
    init {
        require(splitDepth <= 30) { "required: split_depth <= 30, actual: $splitDepth" }
    }

    public object Tlb : TlbCodec<DepthBalanceInfo> {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: DepthBalanceInfo
        ): Unit = cellBuilder {
            storeUIntLeq(value.splitDepth, 30)
            storeTlb(CurrencyCollection.Tlb, value.balance)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): DepthBalanceInfo = cellSlice {
            val splitDepth = loadUIntLeq(30).toInt()
            val balance = loadTlb(CurrencyCollection.Tlb)
            DepthBalanceInfo(splitDepth, balance)
        }
    }

    public companion object {
        public const val BITS: Int = 5
    }
}
