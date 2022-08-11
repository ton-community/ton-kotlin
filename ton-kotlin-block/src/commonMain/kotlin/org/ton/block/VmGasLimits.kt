@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("gas_limits")
@Serializable
data class VmGasLimits(
    val remaining: Long,
    val max_limit: Long,
    val cur_limit: Long,
    val credit: Long
)
