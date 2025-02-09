package org.ton.block

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.currency.VarUInt248
import org.ton.kotlin.dict.Dictionary
import org.ton.kotlin.dict.DictionaryKeyCodec
import org.ton.kotlin.dict.RawDictionary
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

/**
 * Dictionary with amounts for multiple currencies.
 *
 * @see [CurrencyCollection]
 */
public class ExtraCurrencyCollection : Dictionary<Int, VarUInt248> {
    public constructor() : super(null, DictionaryKeyCodec.INT32, VarUInt248)

    public constructor(map: Map<Int, VarUInt248>, context: CellContext = CellContext.EMPTY) : super(
        map, DictionaryKeyCodec.INT32, VarUInt248, context
    )

    public constructor(cell: Cell?, context: CellContext = CellContext.EMPTY) : super(
        cell, DictionaryKeyCodec.INT32, VarUInt248, context
    )

    public constructor(rawDictionary: RawDictionary, context: CellContext = CellContext.EMPTY) : super(
        rawDictionary, DictionaryKeyCodec.INT32, VarUInt248, context
    )

    public constructor(dictionary: Dictionary<Int, VarUInt248>, context: CellContext = CellContext.EMPTY) : super(
        dictionary.dict.root, DictionaryKeyCodec.INT32, VarUInt248, context
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int = super.hashCode()

    override fun toString(): String = this.asSequence().joinToString { (key, value) -> "$key=$value" }

    public companion object : TlbConstructorProvider<ExtraCurrencyCollection> by ExtraCurrencyCollectionTlbConstructor {
        public val EMPTY: ExtraCurrencyCollection = ExtraCurrencyCollection(null)
    }
}

private object ExtraCurrencyCollectionTlbConstructor : TlbConstructor<ExtraCurrencyCollection>(
    schema = "extra_currencies\$_ dict:(HashmapE 32 (VarUInteger 32)) = ExtraCurrencyCollection;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: ExtraCurrencyCollection
    ) {
        val root = value.dict.root
        if (root == null) {
            cellBuilder.storeBoolean(false)
        } else {
            cellBuilder.storeBoolean(true)
            cellBuilder.storeRef(root)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExtraCurrencyCollection {
        val root = if (cellSlice.loadBoolean()) cellSlice.loadRef() else null
        return ExtraCurrencyCollection(root)
    }
}
