package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
data class ValueFlow(
    val from_prev_blk: CurrencyCollection,
    val to_next_blk: CurrencyCollection,
    val imported: CurrencyCollection,
    val exported: CurrencyCollection,
    val fees_collected: CurrencyCollection,
    val fees_imported: CurrencyCollection,
    val recovered: CurrencyCollection,
    val created: CurrencyCollection,
    val minted: CurrencyCollection
) {
    companion object : TlbCodec<ValueFlow> by ValueFlowTlbConstructor.asTlbCombinator()
}

private object ValueFlowTlbConstructor : TlbConstructor<ValueFlow>(
    schema = "value_flow#b8e48dfb ^[ from_prev_blk:CurrencyCollection " +
            "to_next_blk:CurrencyCollection " +
            "imported:CurrencyCollection " +
            "exported:CurrencyCollection ] " +
            "fees_collected:CurrencyCollection " +
            "^[ " +
            "fees_imported:CurrencyCollection " +
            "recovered:CurrencyCollection " +
            "created:CurrencyCollection " +
            "minted:CurrencyCollection " +
            "] = ValueFlow;"
) {
    val currencyCollection by lazy { CurrencyCollection.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: ValueFlow
    ) = cellBuilder {
        storeRef {
            storeTlb(currencyCollection, value.from_prev_blk)
            storeTlb(currencyCollection, value.to_next_blk)
            storeTlb(currencyCollection, value.imported)
            storeTlb(currencyCollection, value.exported)
        }
        storeTlb(currencyCollection, value.fees_collected)
        storeRef {
            storeTlb(currencyCollection, value.fees_imported)
            storeTlb(currencyCollection, value.recovered)
            storeTlb(currencyCollection, value.created)
            storeTlb(currencyCollection, value.minted)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ValueFlow = cellSlice {
        val (fromPrevBlk, toNextBlk, imported, exported) = loadRef {
            arrayOf(
                loadTlb(currencyCollection),
                loadTlb(currencyCollection),
                loadTlb(currencyCollection),
                loadTlb(currencyCollection),
            )
        }
        val feesCollected = loadTlb(currencyCollection)
        val (feesImported, recovered, created, minted) = loadRef {
            arrayOf(
                loadTlb(currencyCollection),
                loadTlb(currencyCollection),
                loadTlb(currencyCollection),
                loadTlb(currencyCollection),
            )
        }
        ValueFlow(fromPrevBlk, toNextBlk, imported, exported, feesCollected, feesImported, recovered, created, minted)
    }
}