package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("trans_split_prepare")
data class TransSplitPrepare(
    val split_info: SplitMergeInfo,
    val storage_ph: Maybe<TrStoragePhase>,
    val compute_ph: TrComputePhase,
    val action: Maybe<TrActionPhase>,
    val aborted: Boolean,
    val destroyed: Boolean
) : TransactionDescr
