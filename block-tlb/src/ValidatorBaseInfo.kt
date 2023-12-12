package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("validator_base_info")
public data class ValidatorBaseInfo(
    val validator_list_hash_short: Long,
    val catchain_seqno: Long
)
