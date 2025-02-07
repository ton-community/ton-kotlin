package org.ton.kotlin.shard

import org.ton.kotlin.currency.CurrencyCollection

/**
 * Intermediate balance info.
 *
 * ```tlb
 * depth_balance$_ split_depth:(#<= 30) balance:CurrencyCollection = DepthBalanceInfo;
 * ```
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

    public companion object {
        public const val BITS: Int = 5
    }
}
