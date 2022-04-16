package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tr_phase_storage")
data class TrStoragePhase(
    val storage_fees_collected: Grams,
    val storage_fees_due: Grams?,
    val status_change: org.ton.block.AccStatusChange
)