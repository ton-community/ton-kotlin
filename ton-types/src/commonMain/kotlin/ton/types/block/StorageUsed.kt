package ton.types.block

import kotlinx.serialization.Serializable

@Serializable
data class StorageUsed(
    val cells: VarUInteger,
    val bits: VarUInteger,
    val publicCells: VarUInteger,
)