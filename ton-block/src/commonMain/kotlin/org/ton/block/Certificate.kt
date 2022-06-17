package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("certificate")
data class Certificate(
    val temp_key: SigPubKey,
    val valid_since: Long,
    val valid_until: Long
)
