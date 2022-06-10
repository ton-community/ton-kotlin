package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("trans_merge_install")
data class TransMergeInstall(
    val split_info: SplitMergeInfo,
    val prepare_transaction: Transaction,
    val storage_ph: Maybe<TrStoragePhase>,
    val credit_ph: Maybe<TrCreditPhase>,
    val compute_ph: TrComputePhase,
    val action: Maybe<TrActionPhase>,
    val aborted: Boolean,
    val destroyed: Boolean
) : TransactionDescr
