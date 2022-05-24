package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("storage_used")
@Serializable
data class StorageUsed(
        val cells: VarUInteger,
        val bits: VarUInteger,
        @SerialName("public_cells")
        val publicCells: VarUInteger,
)
