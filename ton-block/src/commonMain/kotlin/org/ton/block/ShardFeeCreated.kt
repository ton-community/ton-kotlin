package org.ton.block

import kotlinx.serialization.Serializable

@Serializable
data class ShardFeeCreated(
    val fees: CurrencyCollection,
    val create: CurrencyCollection
)