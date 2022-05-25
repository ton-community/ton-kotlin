package org.ton.block.tlb

import org.ton.block.Coins
import org.ton.block.CurrencyCollection
import org.ton.block.ExtraCurrencyCollection
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun CurrencyCollection.Companion.tlbCodec(): TlbCodec<CurrencyCollection> = CurrencyCollectionTlbConstructor()

private class CurrencyCollectionTlbConstructor : TlbConstructor<CurrencyCollection>(
    schema = "currencies\$_ coins:Coins other:ExtraCurrencyCollection = CurrencyCollection;"
) {
    private val coinsCodec by lazy {
        Coins.tlbCodec()
    }
    private val extraCurrencyCollectionCodec by lazy {
        ExtraCurrencyCollection.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: CurrencyCollection
    ) = cellBuilder {
        storeTlb(coinsCodec, value.coins)
        storeTlb(extraCurrencyCollectionCodec, value.other)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): CurrencyCollection = cellSlice {
        val coins = loadTlb(coinsCodec)
        val other = loadTlb(extraCurrencyCollectionCodec)
        CurrencyCollection(coins, other)
    }
}
