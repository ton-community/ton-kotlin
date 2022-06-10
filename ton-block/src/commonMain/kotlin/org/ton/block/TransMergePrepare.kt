package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("trans_merge_prepare")
data class TransMergePrepare(
    val split_info: SplitMergeInfo,
    val storage_ph: TrStoragePhase,
    val aborted: Boolean
) : TransactionDescr
