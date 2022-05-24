package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("storage_info")
@Serializable
data class StorageInfo(
    val used: StorageUsed,
    @SerialName("last_paid")
    val lastPaid: Int,
    @SerialName("due_payment")
    val duePayment: Maybe<Coins>
) {
    constructor(used: StorageUsed, lastPaid: Int, duePayment: Coins? = null) : this(
        used,
        lastPaid,
        duePayment.toMaybe()
    )
}
