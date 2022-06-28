package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("currencies")
@Serializable
data class CurrencyCollection(
    val coins: Coins,
    val other: ExtraCurrencyCollection = ExtraCurrencyCollection()
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<CurrencyCollection> = CurrencyCollectionTlbConstructor
    }
}

private object CurrencyCollectionTlbConstructor : TlbConstructor<CurrencyCollection>(
    schema = "currencies\$_ coins:Coins other:ExtraCurrencyCollection = CurrencyCollection;"
) {
    private val coinsCodec = Coins.tlbCodec()
    private val extraCurrencyCollectionCodec = ExtraCurrencyCollection.tlbCodec()

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
