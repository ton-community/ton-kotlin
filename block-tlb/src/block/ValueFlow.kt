package org.ton.kotlin.block

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer
import org.ton.kotlin.currency.CurrencyCollection

/**
 * Coins flow info.
 */
public data class ValueFlow(
    /**
     * Total amount transferred from the previous block.
     */
    val fromPrevBlock: CurrencyCollection,

    /**
     * Total amount transferred to the next block.
     */
    val toNextBlock: CurrencyCollection,

    /**
     * Sum of all imported amounts from messages.
     */
    val imported: CurrencyCollection,

    /**
     * Sum of all exported amounts of messages.
     */
    val exported: CurrencyCollection,

    /**
     * Total fees collected in this block.
     */
    val feesCollected: CurrencyCollection,

    /**
     * Shard fees imported to this block.
     */
    val feesImported: CurrencyCollection,

    /**
     * Sum of all burned amounts
     */
    val burned: CurrencyCollection?,

    /**
     * TODO: documentation
     */
    val recovered: CurrencyCollection,

    /**
     * Block creation fees.
     */
    val created: CurrencyCollection,

    /**
     * Minted extra currencies.
     */
    val minted: CurrencyCollection,
) {
    public companion object : CellSerializer<ValueFlow> by ValueFlowSerializer
}

private object ValueFlowSerializer : CellSerializer<ValueFlow> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): ValueFlow {
        TODO("Not yet implemented")
    }

    override fun store(
        builder: CellBuilder,
        value: ValueFlow,
        context: CellContext
    ) {
        TODO("Not yet implemented")
    }
}