package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("currencies")
@Serializable
data class CurrencyCollection(
        val coins: Coins,
        val other: ExtraCurrencyCollection
)