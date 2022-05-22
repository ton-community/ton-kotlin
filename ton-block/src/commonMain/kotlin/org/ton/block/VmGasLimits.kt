package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("gas_limits")
@Serializable
data class VmGasLimits(
    val remaining: Long,
    @SerialName("max_limit")
    val maxLimit: Long,
    @SerialName("cur_limit")
    val curLimit: Long,
    val credit: Long
)
