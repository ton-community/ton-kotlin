@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface TransactionDescr {
    @SerialName("trans_ord")
    @Serializable
    data class TransOrd(
        val credit_first: Boolean,
        val storage_ph: TrStoragePhase?,
        val credit_ph: TrCreditPhase?,
        val compute_ph: TrComputePhase,
        val action: TrActionPhase,
        val aborted: Boolean,
        val bounce: TrBouncePhase?,
        val destroyed: Boolean
    ) : TransactionDescr

    @SerialName("trans_storage")
    @Serializable
    data class TransStorage(
        val storage_ph: TrStoragePhase
    ) : TransactionDescr
}