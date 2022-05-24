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

fun CurrencyCollection.Companion.tlbCodec(): TlbCodec<CurrencyCollection> = CurrencyCollectionTlbConstructor

private object CurrencyCollectionTlbConstructor : TlbConstructor<CurrencyCollection>(
    schema = "currencies\$_ coins:Coins other:ExtraCurrencyCollection = CurrencyCollection;"
) {
    private val coinsCodec = Coins.tlbCodec()
    private val extraCurrencyCollectionCodec = ExtraCurrencyCollection.tlbCodec()

    override fun encode(
        cellBuilder: CellBuilder, value: CurrencyCollection, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.coins, coinsCodec)
        storeTlb(value.other, extraCurrencyCollectionCodec)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): CurrencyCollection = cellSlice {
        val coins = loadTlb(coinsCodec)
        val other = loadTlb(extraCurrencyCollectionCodec)
        CurrencyCollection(coins, other)
    }
}
