package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("tr_phase_credit")
@Serializable
data class TrCreditPhase(
    val due_fees_collected: Grams,
    val credit: CurrencyCollection
)