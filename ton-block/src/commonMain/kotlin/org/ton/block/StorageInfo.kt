package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("storage_info")
@Serializable
data class StorageInfo(
        val used: StorageUsed,
        val last_paid: Int,
        val due_payment: Coins?
)