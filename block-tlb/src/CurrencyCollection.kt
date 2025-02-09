package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.kotlin.currency.VarUInt248
import org.ton.kotlin.dict.Dictionary
import org.ton.tlb.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

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
) : TlbObject {
    public constructor() : this(Coins.ZERO, ExtraCurrencyCollection.EMPTY)

    public constructor(coins: Coins) : this(coins, ExtraCurrencyCollection.EMPTY)

    public constructor(coins: Coins, other: Map<Int, VarUInt248>) : this(coins, ExtraCurrencyCollection(other))

    public constructor(coins: Coins, other: Dictionary<Int, VarUInt248>) : this(coins, ExtraCurrencyCollection(other))

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("currencies") {
        field("coins", coins)
        field("other", other)
    }

    override fun toString(): String =
        if (other.isEmpty()) {
            coins.toString()
        } else buildString {
            append("(")
            append(coins)
            other.forEach { (key, value) ->
                append("+")
                append(value)
                append(".$")
                append(key)
            }
            append(")")
        }

    public companion object : TlbConstructorProvider<CurrencyCollection> by CurrencyCollectionTlbConstructor {
        public val ZERO: CurrencyCollection = CurrencyCollection(Coins.ZERO, ExtraCurrencyCollection.EMPTY)
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
