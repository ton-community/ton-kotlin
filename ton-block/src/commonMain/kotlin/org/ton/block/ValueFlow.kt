package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.*
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
    override fun storeTlb(
        cellBuilder: CellBuilder, value: ValueFlow
    ) = cellBuilder {
        storeRef {
            storeTlb(CurrencyCollection, value.from_prev_blk)
            storeTlb(CurrencyCollection, value.to_next_blk)
            storeTlb(CurrencyCollection, value.imported)
            storeTlb(CurrencyCollection, value.exported)
        }
        storeTlb(CurrencyCollection, value.fees_collected)
        storeRef {
            storeTlb(CurrencyCollection, value.fees_imported)
            storeTlb(CurrencyCollection, value.recovered)
            storeTlb(CurrencyCollection, value.created)
            storeTlb(CurrencyCollection, value.minted)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ValueFlow = cellSlice {
        val (fromPrevBlk, toNextBlk, imported, exported) = loadRef {
            arrayOf(
                loadTlb(CurrencyCollection),
                loadTlb(CurrencyCollection),
                loadTlb(CurrencyCollection),
                loadTlb(CurrencyCollection),
            )
        }
        val feesCollected = loadTlb(CurrencyCollection)
        val (feesImported, recovered, created, minted) = loadRef {
            arrayOf(
                loadTlb(CurrencyCollection),
                loadTlb(CurrencyCollection),
                loadTlb(CurrencyCollection),
                loadTlb(CurrencyCollection),
            )
        }
        ValueFlow(fromPrevBlk, toNextBlk, imported, exported, feesCollected, feesImported, recovered, created, minted)
    }
}