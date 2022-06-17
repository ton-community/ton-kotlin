package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ihr_pending")
data class IhrPendingSince(
    val import_lt: Long
)
