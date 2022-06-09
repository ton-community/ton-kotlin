package org.ton.block

import kotlinx.serialization.Serializable

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
)
