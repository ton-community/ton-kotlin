package org.ton.kotlin.currency

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer

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

    public companion object : CellSerializer<CurrencyCollection> by CurrencyCollectionSerializer {
        public val ZERO: CurrencyCollection = CurrencyCollection(Coins.ZERO)
    }
}

private object CurrencyCollectionSerializer : CellSerializer<CurrencyCollection> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): CurrencyCollection {
        TODO()
    }

    override fun store(
        builder: CellBuilder,
        value: CurrencyCollection,
        context: CellContext
    ) {
        TODO()
    }
}