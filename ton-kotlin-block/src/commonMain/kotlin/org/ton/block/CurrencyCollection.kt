package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("currencies")
@Serializable
data class CurrencyCollection(
    val coins: Coins,
    val other: ExtraCurrencyCollection = ExtraCurrencyCollection()
) {
    override fun toString(): String = "currencies(coins:$coins other:$other)"

    companion object : TlbCodec<CurrencyCollection> by CurrencyCollectionTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<CurrencyCollection> = CurrencyCollectionTlbConstructor
    }
}

private object CurrencyCollectionTlbConstructor : TlbConstructor<CurrencyCollection>(
    schema = "currencies\$_ coins:Coins other:ExtraCurrencyCollection = CurrencyCollection;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: CurrencyCollection
    ) = cellBuilder {
        storeTlb(Coins, value.coins)
        storeTlb(ExtraCurrencyCollection, value.other)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): CurrencyCollection = cellSlice {
        val coins = loadTlb(Coins)
        val other = loadTlb(ExtraCurrencyCollection)
        CurrencyCollection(coins, other)
    }
}
