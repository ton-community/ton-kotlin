package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImportFees(
    @SerialName("fees_collected")
    val feesCollected: Coins,
    @SerialName("value_imported")
    val valueImported: CurrencyCollection
)
