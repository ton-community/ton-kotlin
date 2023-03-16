package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider
import kotlin.jvm.JvmName

@SerialName("currencies")
@Serializable
public data class CurrencyCollection(
    @get:JvmName("coins")
    val coins: Coins, // coins: Coins

    @get:JvmName("other")
    val other: ExtraCurrencyCollection // other: ExtraCurrencyCollection
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("currencies") {
        field("coins", coins)
        field("other", other)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<CurrencyCollection> by CurrencyCollectionTlbConstructor
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
