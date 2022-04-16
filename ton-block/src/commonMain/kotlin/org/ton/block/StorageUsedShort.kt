package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("storage_used_short")
@Serializable
data class StorageUsedShort(
    val cells: VarUInteger,
    val bits: VarUInteger
)