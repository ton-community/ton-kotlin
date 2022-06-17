package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("validator_info")
data class ValidatorInfo(
    val validator_list_hash_short: Long,
    val catchain_seqno: Long,
    val nx_cc_updated: Boolean
)
