package ton.types.block

import kotlinx.serialization.Serializable

@Serializable
data class StorageInfo(
    val used: StorageUsed,
    val lastPaid: Int,
    val duePayment: Grams?
)