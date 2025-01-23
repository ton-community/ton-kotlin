package org.ton.block.currency

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

/**
 * Amounts collection.
 */
public data class CurrencyCollection(
    /**
     * Amount in native currency.
     */
    val coins: Coins,

    /**
     * Amounts in other currencies.
     */
    val other: ExtraCurrencyCollection
) {
    public constructor(coins: Coins) : this(coins, ExtraCurrencyCollection())

    public object Tlb : TlbCodec<CurrencyCollection> {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: CurrencyCollection
        ): Unit = cellBuilder {
            storeTlb(Coins.Tlb, value.coins)
            storeTlb(ExtraCurrencyCollection, value.other)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): CurrencyCollection = cellSlice {
            val coins = loadTlb(Coins.Tlb)
            val other = loadTlb(ExtraCurrencyCollection)
            CurrencyCollection(coins, other)
        }
    }

    public companion object {
        public val ZERO: CurrencyCollection = CurrencyCollection(Coins.ZERO)
    }
}