package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("trans_split_install")
data class TransSplitInstall(
    val split_info: SplitMergeInfo,
    val prepare_transaction: Transaction,
    val installed: Boolean
) : TransactionDescr
