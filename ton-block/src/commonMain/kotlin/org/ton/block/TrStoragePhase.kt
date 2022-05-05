package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tr_phase_storage")
data class TrStoragePhase(
        val storage_fees_collected: Coins,
        val storage_fees_due: Coins?,
        val status_change: AccStatusChange
)